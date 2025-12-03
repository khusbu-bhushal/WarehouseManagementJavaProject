import java.util.*;

public class WaitList {
  private static WaitList INSTANCE;
  // productId -> FIFO queue of requests
  private final Map<String, Deque<WaitListItem>> byProduct = new HashMap<>();

  private WaitList() {}

  public static WaitList instance() {
    if (INSTANCE == null) INSTANCE = new WaitList();
    return INSTANCE;
  }

  public void add(String clientId, String productId, int qty) {
    byProduct.computeIfAbsent(productId, k -> new ArrayDeque<>())
             .addLast(new WaitListItem(clientId, productId, qty));
  }

  public Deque<WaitListItem> getQueue(String productId) {
    return byProduct.getOrDefault(productId, new ArrayDeque<>());
  }

  public boolean has(String productId) {
    Deque<WaitListItem> q = byProduct.get(productId);
    return q != null && !q.isEmpty();
  }

  // Try to fulfill as many waitlist entries as possible for this product.
  // Uses available stock in Product p, creates transactions in 'warehouse'.
  public void fulfill(String productId, Warehouse warehouse) {
    Product p = warehouse.getProduct(productId);
    if (p == null) return;

    Deque<WaitListItem> q = getQueue(productId);
    while (!q.isEmpty() && p.getQuantity() > 0) {
      WaitListItem w = q.peekFirst();
      int fill = Math.min(p.getQuantity(), w.getRequestedQty());

      // deduct stock
      p.removeStock(fill);

      // create a simple one-item order transaction for that client
      Map<String,Integer> m = new HashMap<>();
      m.put(productId, fill);
      double total = p.getPrice() * fill;
      warehouse.addTransaction(new Transaction(w.getClientID(), "waitlist-fulfill", m, total));
      System.out.println("Fulfilled " + fill + " of " + productId + " for client " + w.getClientID());

      // reduce or remove entry
      int remaining = w.getRequestedQty() - fill;
      if (remaining <= 0) q.removeFirst();
      else w.setRequestedQty(remaining);
    }
  }

  @Override public String toString() {
    StringBuilder sb = new StringBuilder("=== Waitlists ===\n");
    for (var e : byProduct.entrySet()) {
      sb.append("Product ").append(e.getKey()).append(":\n");
      for (WaitListItem wi : e.getValue()) {
        sb.append("  ").append(wi).append("\n");
      }
    }
    return sb.toString();
  }
}