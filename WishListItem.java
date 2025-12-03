import java.time.Instant;
import java.util.Objects;

public class WishListItem {       //represents single item in customer's wishlist (one product and its quantity)
    private final Product product;   // reference to catalog product
    private int quantity;            // desired quantity on wishlist
    private final Instant addedAt; //captures timestamp of when item was added to the wishlist

    public WishListItem(Product product, int quantity) {
        if (product == null) throw new IllegalArgumentException("product required");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.product = product;      //store provided product 
        this.quantity = quantity;   //store provided quantity
        this.addedAt = Instant.now(); //record the timestamp
    }

    //returns product object
    public Product getProduct() { return product; } 

    //returns how many units are wanted
    public int getQuantity() { return quantity; }

    //make sure you cant set quantity to 0 or -ve and update quantity
    public void setQuantity(int q) {
        if (q <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.quantity = q;
    }

    //adds incr (number) to existing quantity
    //icr has to be >0
    public void increment(int incr) {
        if (incr <= 0) throw new IllegalArgumentException("incr must be > 0");
        this.quantity += incr;
    }

    //human readable summary. avoids java's defualt string representation
    @Override public String toString() {
        return product.getProductId() + " - " + product.getName() + " x " + quantity;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;    //comparing same object in the memory return true
        if (!(o instanceof WishListItem)) return false;   //if other obj is not wishlistitem, they arent be equal
        WishListItem that = (WishListItem) o; //convert o to wishlistitem to compare fields
        return Objects.equals(product.getProductId(), that.product.getProductId()); 
    }

    //generates hash code consistent with equals method
    @Override public int hashCode() { return Objects.hash(product.getProductId()); }
}
