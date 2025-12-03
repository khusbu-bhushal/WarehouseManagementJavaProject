import java.util.Objects;

public class Product {
    private String productId;
    private String name;
    private int quantity;
    private double price; 

    public Product(String productId, String name, int quantity, double price){

        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product that = (Product) o;
        return Objects.equals(this.productId, that.productId);
    }
    @Override
    public String toString() {
        return productId + " | " + name + " | " + quantity + " | $" + price;
    }

    public int getQuantity(){
        return quantity;
    }

    public String getInfo(){
        return "Product: "+ name + "\n" + "Product ID: " + productId + "\n" + "quantity: "+ quantity + "\n" + "Price: $" + price;
    }

    public String getProductId(){
        return productId;
    }

    public double getPrice(){
        return price;
    }

    public String getName(){
        return name;
    }

    public void addQuantity(int amount){
        quantity += amount;
        System.out.println("Product " + name + "'s Quantity has been increased");
    }

    public void removeStock(int amount){
        quantity -= amount;
        System.out.println("Product " + name + "'s  Quantity has been decreased");
    }

    public void setPrice(double newPrice){
        price = newPrice;
        System.out.println("Product " + name + " has changed in price");
    }
}
