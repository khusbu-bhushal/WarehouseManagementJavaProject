import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ManagerMenuState extends WarehouseState implements ActionListener {

    private static ManagerMenuState managerMenuState;
    private static Warehouse warehouse;

    // GUI components
    private JFrame frame;
    private JButton btnAddProduct;
    private JButton btnDisplayWaitlist;
    private JButton btnReceiveShipment;
    private JButton btnBecomeClerk;
    private JButton btnPrintInvoices;
    private JButton btnLogout;

    // ---- Singleton ----
    private ManagerMenuState() {
        super();
        warehouse = Warehouse.instance();
    }

    public static ManagerMenuState instance() {
        if (managerMenuState == null) {
            managerMenuState = new ManagerMenuState();
        }
        return managerMenuState;
    }

    /* ================== Helper methods (GUI versions) ================== */

    // 0. Add Product
    private void addProductGUI() {
        String name = JOptionPane.showInputDialog(frame, "Enter product name:");
        if (name == null || name.trim().isEmpty()) {
            return;
        }

        String qtyStr = JOptionPane.showInputDialog(frame, "Enter product quantity:");
        if (qtyStr == null || qtyStr.trim().isEmpty()) {
            return;
        }

        String priceStr = JOptionPane.showInputDialog(frame, "Enter product price:");
        if (priceStr == null || priceStr.trim().isEmpty()) {
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyStr.trim());
            double price = Double.parseDouble(priceStr.trim());
            warehouse.addProduct(name.trim(), price, quantity);
            JOptionPane.showMessageDialog(frame,
                    "Product added successfully.",
                    "Add Product",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                    "Invalid quantity or price.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // 1. View Product Waitlist
    private void displayWaitlistGUI() {
        String productId = JOptionPane.showInputDialog(frame, "Enter product ID:");
        if (productId == null || productId.trim().isEmpty()) {
            return;
        }

        String result = warehouse.getWaitlist(productId.trim());
        if (result == null || result.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "No waitlist entries for product " + productId,
                    "Waitlist",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame,
                    result,
                    "Waitlist for product " + productId,
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // 2. Receive Shipment
    private void receiveShipmentGUI() {
        String productId = JOptionPane.showInputDialog(frame, "Enter product ID:");
        if (productId == null || productId.trim().isEmpty()) {
            return;
        }

        String qtyStr = JOptionPane.showInputDialog(frame, "Enter quantity received:");
        if (qtyStr == null || qtyStr.trim().isEmpty()) {
            return;
        }

        try {
            int qty = Integer.parseInt(qtyStr.trim());
            boolean success = warehouse.receiveShipment(productId.trim(), qty);

            if (success) {
                JOptionPane.showMessageDialog(frame,
                        "Shipment recorded.",
                        "Receive Shipment",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Shipment not recorded.",
                        "Receive Shipment",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                    "Invalid quantity.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // 3. Become Clerk
    private void becomeClerkGUI() {
        int transitionToClerk = 2;  // Clerk state index
        WarehouseContext.instance().changeState(transitionToClerk);
    }

    // 4. Show all Customer Invoices
    private void printInvoicesGUI() {
        StringBuilder sb = new StringBuilder();
        for (Transaction t : warehouse.getTransactions()) {
            sb.append(t.getInvoice()).append("\n\n");
        }

        if (sb.length() == 0) {
            sb.append("No invoices available.");
        }

        JOptionPane.showMessageDialog(frame,
                sb.toString(),
                "Customer Invoices",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // 5. Logout
    private void logoutGUI() {
        int transitionToLogin = 0;  // back to Login state
        WarehouseContext.instance().changeState(transitionToLogin);
    }

    /* ================== ActionListener ================== */

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnAddProduct) {
            addProductGUI();
        } else if (src == btnDisplayWaitlist) {
            displayWaitlistGUI();
        } else if (src == btnReceiveShipment) {
            receiveShipmentGUI();
        } else if (src == btnBecomeClerk) {
            becomeClerkGUI();
        } else if (src == btnPrintInvoices) {
            printInvoicesGUI();
        } else if (src == btnLogout) {
            logoutGUI();
        }
    }

    /* ================== State entry (build GUI) ================== */

    @Override
public void run() {
    frame = WarehouseContext.instance().getFrame();
    frame.setTitle("Manager Menu");

    // ----- Root container -----
    Container root = frame.getContentPane();
    root.removeAll();
    root.setLayout(new BorderLayout());

    // ===== Title Label =====
    JLabel titleLabel = new JLabel("MANAGER MENU", SwingConstants.CENTER);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
    root.add(titleLabel, BorderLayout.NORTH);

    // ===== Center Panel for Buttons =====
    JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 15));
    GUIStyle.addDefaultPadding(buttonPanel); // add your nice spacing

    // Buttons
    btnAddProduct       = new JButton("Add Product");
    btnDisplayWaitlist  = new JButton("View Product Waitlist");
    btnReceiveShipment  = new JButton("Receive Shipment");
    btnBecomeClerk      = new JButton("Become Clerk");
    btnPrintInvoices    = new JButton("Show All Customer Invoices");
    btnLogout           = new JButton("Logout");

    // Listeners
    btnAddProduct.addActionListener(this);
    btnDisplayWaitlist.addActionListener(this);
    btnReceiveShipment.addActionListener(this);
    btnBecomeClerk.addActionListener(this);
    btnPrintInvoices.addActionListener(this);
    btnLogout.addActionListener(this);

    // Style buttons
    GUIStyle.styleMainButton(btnAddProduct);
    GUIStyle.styleMainButton(btnDisplayWaitlist);
    GUIStyle.styleMainButton(btnReceiveShipment);
    GUIStyle.styleMainButton(btnBecomeClerk);
    GUIStyle.styleMainButton(btnPrintInvoices);
    GUIStyle.styleMainButton(btnLogout);

    // Add buttons
    buttonPanel.add(btnAddProduct);
    buttonPanel.add(btnDisplayWaitlist);
    buttonPanel.add(btnReceiveShipment);
    buttonPanel.add(btnBecomeClerk);
    buttonPanel.add(btnPrintInvoices);
    buttonPanel.add(btnLogout);

    // Add panel to center
    root.add(buttonPanel, BorderLayout.CENTER);

    // Finish
    frame.setSize(800, 400);
    frame.revalidate();
    frame.repaint();
    frame.setVisible(true);
    frame.toFront();
    frame.requestFocus();
}
}
