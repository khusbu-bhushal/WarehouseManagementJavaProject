import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map;



public class Transaction {

    
    private final String transactionId;     
    private final String actorClientId;     // Client performing the transaction
    private final String type;              // e.g., "Purchase"
    private LocalDateTime timestamp;        
    private final Map<String, Integer> productQtys;       // Simple product list (e.g., "P001 x2")
    private double totalAmount;             // Total transaction value
    private boolean isCompleted;
    private double total;            

    public Transaction(String actorClientId, String type, Map<String, Integer> productQtys, double total) {
        this.transactionId = generateUniqueTransactionId(actorClientId);
        this.actorClientId = actorClientId;
        this.type = type;
        this.productQtys = productQtys;
        this.isCompleted = false;
        this.total = total;
        this.timestamp = java.time.LocalDateTime.now();  
    }

    private String generateUniqueTransactionId(String clientId) {
        return clientId + "-TXN-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public String getId() { return transactionId; }
    public String getType() { return type; }
    public Double getTotal() { return total; }
    public String getActorClientId() { return actorClientId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Map<String, Integer> getProductQtys() { return productQtys; }
    public double getTotalAmount() { return totalAmount; }

    public void startTransaction() {
        this.timestamp = LocalDateTime.now();
        System.out.println("Transaction " + transactionId + " started at " + timestamp);
    }

    public void addProduct(String productId, int quantity, double unitPrice) {
    if (productQtys == null) return;        
    productQtys.merge(productId, quantity, Integer::sum);
    totalAmount += unitPrice * quantity;
}

    public void completeTransaction() {
    this.isCompleted = true;
}

public String getInvoice() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n=== INVOICE ===\n");
    sb.append("Client ID: ").append(actorClientId).append("\n");
    sb.append("Transaction ID: ").append(transactionId).append("\n");
    sb.append("Date: ").append(timestamp).append("\n\n");
    sb.append("Items:\n");
    if (productQtys != null) {
        for (Map.Entry<String, Integer> e : productQtys.entrySet()) {
            sb.append("  - ").append(e.getKey()).append(" x").append(e.getValue()).append("\n");
        }
    }
    sb.append("Total Amount: $").append(total).append("\n");
    sb.append("=====================\n");
    return sb.toString();
}



    
    
}
