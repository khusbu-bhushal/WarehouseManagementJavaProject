import java.util.*;

public class WishList {
    // List of WishListItem (one entry per product)

    private final List<WishListItem> items = new ArrayList<>();
    private final String id; // client ID

//Constructor that creates a wishlist for clientID. It starts with empty item list
    public WishList(String clientID) {
        this.id = clientID;
    }

    public void add(Product p, int quantity) {
        if (p == null) throw new IllegalArgumentException("Product cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");

        WishListItem existing = findItemByProductId(p.getProductId());
        if (existing != null) existing.increment(quantity);
        else items.add(new WishListItem(p, quantity));
    }

    // does the Wishlist contains the product? 
    public boolean hasProduct(Product p) {
        if (p == null) return false;
        return findItemByProductId(p.getProductId()) != null;
    }

    // Return list of WishListItems (copy) Items in WishListItem objects stay same
    public List<WishListItem> getItems() {
        return new ArrayList<>(items);
    }

    public void clearWishList(){
        items.clear();
    }
    public void clear() { items.clear(); }

    
    public List<Product> getProducts() {       //defining get products
        List<Product> prods = new ArrayList<>();   //creates object prods
        for (WishListItem currentWishListItem : items) {     //for each item inside items, assigns the item to variable wi and execute code body
            prods.add(currentWishListItem.getProduct());   
        }
        return prods;    //returning arraylist of products extracted from items array list
    }


 
    //returns the wishlist's owner ID
    public String getClientId() { return id; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WishList{clientId='").append(id).append("', items=[");
        for (WishListItem wi : items) {
            sb.append(wi.toString()).append(", ");
        }
        if (!items.isEmpty()) sb.setLength(sb.length() - 2); // remove last comma
        sb.append("]}");
        return sb.toString();
    }

    public void printPretty() {
        System.out.println("WishList for client " + id + ":");
        for (WishListItem wi : items) {
            System.out.println("  " + wi);
        }
    }

    //stops after removing first matching item 
    public boolean remove(String productId) {
    if (productId == null) return false;
    for (int i = 0; i < items.size(); i++) {
        WishListItem wi = items.get(i);
        if (wi.getProduct().getProductId().equals(productId)) {
            items.remove(i);
            return true;
        }
    }
    return false;
}


    // Helper method to find item by Product ID
    private WishListItem findItemByProductId(String pid) {
        for (WishListItem wi : items) {
            if (wi.getProduct().getProductId().equals(pid)) {
                return wi;
            }
        }
        return null;
    }
}