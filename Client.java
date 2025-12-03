public class Client {
    private String clientID;
    private String name;
    private String address;
    private Double balance;
    private Boolean frozen;
    public WishList wishlist;

    // Constructor
    public Client(String clientID, String name, String address, Double balance) {
        if (clientID == null || clientID.isEmpty()) {
            throw new IllegalArgumentException("ClientID required");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name required");
        }
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address required");
        }
        this.clientID = clientID;
        this.name = name; 
        this.address = address;
        this.balance = balance;
        this.frozen = false;
        this.wishlist = new WishList(clientID);
    }

    // Getters
    public String getId() { return clientID;  }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public Double getBalance() { return balance; }
    public Boolean isFrozen() { return frozen; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setBalance(Double balance) { this.balance = balance; }
    public void setFrozen(Boolean frozen) { this.frozen = frozen; }

    public WishList getWishList(){
        return wishlist;
    }

    public void adjustBalance(Double amount){
        balance += amount;
    }

    @Override
    public String toString() {
        return clientID + " | " + name + " | " + address+ " | " + "$" + balance;
    }
} 
