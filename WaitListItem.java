public class WaitListItem {
  private final String clientID;
  private final String productID;
  private int requestedQty;

  public WaitListItem(String clientID, String productID, int requestedQty) {
    if (clientID == null || clientID.isEmpty()) throw new IllegalArgumentException("clientID required");
    if (productID == null || productID.isEmpty()) throw new IllegalArgumentException("productID required");
    if (requestedQty <= 0) throw new IllegalArgumentException("requestedQty must be > 0");
    this.clientID = clientID;
    this.productID = productID;
    this.requestedQty = requestedQty;
  }

  public String getClientID() { return clientID; }
  public String getProductID() { return productID; }
  public int getRequestedQty() { return requestedQty; }
  public void setRequestedQty(int q) { if (q <= 0) throw new IllegalArgumentException("qty > 0"); this.requestedQty = q; }

  @Override public String toString() {
    return "WaitListItem{client=" + clientID + ", product=" + productID + ", qty=" + requestedQty + "}";
  }
}