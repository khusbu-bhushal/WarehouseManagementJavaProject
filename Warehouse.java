import java.util.*;

public class Warehouse {

    
    private final List<Client> clients = new ArrayList<>();
    private final ProductList products = new ProductList();
    private final List<Transaction> transactions = new ArrayList<>();
    private final WaitList waitList = WaitList.instance();
    private static Warehouse warehouse;
    void addTransaction(Transaction t) { transactions.add(t); } // used by WaitList.fulfill



    private Warehouse() {
    }

    public static Warehouse instance() {
    if (warehouse == null) {
      return (warehouse = new Warehouse());
    } else {
      return warehouse;
        }
    }
   
    public void addClient(String name, String address) {
        String id = "C" + "000"+ (clients.size() + 1);
        Client client = new Client(id, name, address, 0.00);
        WishList wishlist = new WishList(id);
        clients.add(client);
        System.out.println("Client " + id + " added.");
    }

    public Client getClient(String clientId) {
        for (Client c : clients) {
            if (c.getId().equals(clientId)) return c;
        }
        return null;
    }

    public void removeClient(String clientId) {
        clients.removeIf(c -> c.getId().equals(clientId));
    }

    public List<Client> listClients() {
        return new ArrayList<>(clients);
    }

    public void freezeClient(String clientId) {
        Client c = getClient(clientId);
        if (c != null) c.setFrozen(true);
    }

    public void unfreezeClient(String clientId) {
        Client c = getClient(clientId);
        if (c != null) c.setFrozen(false);
    }

    
    public void addProduct(String name, double price, int qty) {
        String id = "P" + "000" + (products.size() +1);
        Product product = new Product(id, name, qty, price);
        products.add(product);
        System.out.println("Product " + id + " added."); 
    }

    public Product getProduct(String productId) {
        for (Product p : products.all()) {
            if (p.getProductId().equals(productId)) return p;
        }
        return null;
    }

    public void removeProduct(String productId) {
        products.remove(productId);
    }

    public List<Product> listProducts() {
        return products.all();
    }

    public void setPrice(String productId, double newPrice) {
        Product p = getProduct(productId);
        if (p != null) p.setPrice(newPrice);
    }

    public double checkPrice(String productId) {
        Product p = getProduct(productId);
        return (p != null) ? p.getPrice() : -1;
    }

    
    public int getOnHand(String productId) {
        Product p = getProduct(productId);
        return (p != null) ? p.getQuantity() : 0;
    }

    public boolean receiveShipment(String productId, int qty) {
        Product p = getProduct(productId);
        if (p != null) {
            p.addQuantity(qty);
            System.out.println("Received " + qty + " units of " + p.getName());
            waitList.fulfill(productId, this);
            return true;          // success
        } else {
            System.out.println("Product not found.");
            return false;         // failure
        }
    }

    public void consumeStock(String productId, int qty) {
        Product p = getProduct(productId);
        if (p != null) p.removeStock(qty);
    }

public void orderWishlist(String clientId) {
    Client c = getClient(clientId);
    if (c == null) {
        System.out.println("Client not found.");
        return;
    }

    WishList wishlist = c.getWishList();
    if (wishlist == null) {
        System.out.println("Wishlist is empty for client " + clientId);
        return;
    }

    Map<String, Integer> productQtys = new HashMap<>();
    for (WishListItem item : wishlist.getItems()) {
        Product p = item.getProduct();
        int qty = item.getQuantity();
        if (p != null && qty > 0) {
            productQtys.put(p.getProductId(), qty);
        }
    }

    // Use the partial-fulfillment logic
    placeOrder(clientId, productQtys);

    // Clear wishlist after processing
    wishlist.clear();
    System.out.println("Wishlist for client " + clientId + " has been processed and cleared.");
}

    public void addToWishList(String clientId, String productId, int qty) {
    Client c = getClient(clientId);
    if (c == null) return;

    Product p = getProduct(productId);
    if (p == null) return;

    c.getWishList().add(p, qty); // uses  existing WishList.add(Product,int)
}


    public void removeFromWishList(String clientId, String productId) {
        Client c = getClient(clientId);
        if (c != null) c.getWishList().remove(productId);
    }

    public WishList getWishList(String clientId) {
        Client c = getClient(clientId);
        return (c != null) ? c.getWishList() : null;
    }


    public String getWaitlist(String productId) {
        return waitList.getQueue(productId).toString();
    }

   
    public void placeOrder(String clientId, Map<String, Integer> productQtys) {
        Client c = getClient(clientId);
        if (c == null) {
            System.out.println("Client not found.");
            return;
        }

        double total = 0.0;
        Map<String,Integer> fulfilled = new HashMap<>();
        Map<String,Integer> waitlisted = new HashMap<>();

        for (var entry : productQtys.entrySet()) {
            String pid = entry.getKey();
            int requested = entry.getValue();
            Product p = getProduct(pid);
            if (p == null) {
                System.out.println("Product not found: " + pid);
                continue;
            }

            int available = p.getQuantity();
            if (available >= requested) {
                // full fill
                p.removeStock(requested);
                fulfilled.put(pid, requested);
                total += p.getPrice() * requested;
            } else if (available > 0) {
                // partial fill, leftover to waitlist
                p.removeStock(available);
                fulfilled.put(pid, available);
                int leftover = requested - available;
                waitList.add(clientId, pid, leftover);
                waitlisted.put(pid, leftover);
                total += p.getPrice() * available;
                System.out.println("Partial: fulfilled " + available + " of " + pid + ", waitlisted " + leftover);
            } else {
                // zero available: all to waitlist
                waitList.add(clientId, pid, requested);
                waitlisted.put(pid, requested);
                System.out.println("Out of stock: waitlisted " + requested + " of " + pid);
            }
        }

        // bill only fulfilled items
        if (!fulfilled.isEmpty()) {
            c.adjustBalance(total);
            transactions.add(new Transaction(clientId, "order", fulfilled, total));
            System.out.println("Order complete. Total: $" + total);
        } else {
            System.out.println("Nothing fulfilled. Entire request waitlisted.");
        }

        if (!waitlisted.isEmpty()) {
            System.out.println("Items placed on waitlist: " + waitlisted);
        }
    }

    public boolean receivePayment(String clientId, double amount) {
        Client c = getClient(clientId);
        if (c == null) {
            System.out.println("Client not found.");
            return false;
        }

        c.adjustBalance(-amount);
        transactions.add(new Transaction("Payment", clientId, null, -amount));
        System.out.println("Received payment of $" + amount + " from " + c.getName());
        return true;
    }

        public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }


    public List<Product> searchProducts(String query) {
        List<Product> results = new ArrayList<>();
        for (Product p : products.all()) {
            if (p.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(p);
            }
        }
        return results;
    }
}
