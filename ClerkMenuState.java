import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ClerkMenuState extends WarehouseState implements ActionListener {

    private static ClerkMenuState clerkMenuState;
    private static Warehouse warehouse;

    // GUI Components
    private JFrame frame;
    private JButton btnLogout;
    private JButton btnAddClient;
    private JButton btnShowProducts;
    private JButton btnShowClients;
    private JButton btnShowClientsWithBalance;
    private JButton btnRecordPayment;
    private JButton btnBecomeClient;

    private ClerkMenuState() {
        super();
        warehouse = Warehouse.instance();
    }

    public static ClerkMenuState instance() {
        if (clerkMenuState == null) {
            clerkMenuState = new ClerkMenuState();
        }
        return clerkMenuState;
    }

    /* ======================  GUI Helper Methods  ====================== */

    
    private void addClientGUI() {
    boolean more = true;

    while (more) {
        String name = JOptionPane.showInputDialog(frame, "Enter client name:");
        if (name == null || name.trim().isEmpty()) return;

        String address = JOptionPane.showInputDialog(frame, "Enter client address:");
        if (address == null || address.trim().isEmpty()) return;

        warehouse.addClient(name.trim(), address.trim());

        int option = JOptionPane.showConfirmDialog(
                frame,
                "Client added successfully. Would you like to add another Client?",
                "Client Added",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (option != JOptionPane.YES_OPTION) {
            more = false;   // stop the loop
        }
    }
}


    private void showProductsGUI() {
        StringBuilder sb = new StringBuilder("--- Product List (qty & price) ---\n");
        for (Product p : warehouse.listProducts()) {
            sb.append(p.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString(), "Products", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showClientsGUI() {
        StringBuilder sb = new StringBuilder("--- All Clients ---\n");
        for (Client c : warehouse.listClients()) {
            sb.append(c.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString(), "Clients", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showClientsWithBalanceGUI() {
        StringBuilder sb = new StringBuilder("--- Clients With Outstanding Balance ---\n");
        for (Client c : warehouse.listClients()) {
            if (c.getBalance() > 0) {
                sb.append(c.toString()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(frame, sb.toString(), "Client Balances", JOptionPane.INFORMATION_MESSAGE);
    }

    private void recordPaymentGUI() {
        String clientId = JOptionPane.showInputDialog(frame, "Enter client ID:");
        if (clientId == null || clientId.trim().isEmpty()) return;

        String amountStr = JOptionPane.showInputDialog(frame, "Enter payment amount:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        try {
            double amount = Double.parseDouble(amountStr.trim());
            boolean ok = warehouse.receivePayment(clientId.trim(), amount);

            if (ok) {
                JOptionPane.showMessageDialog(frame, "Payment recorded.");
            } else {
                JOptionPane.showMessageDialog(frame, "Payment failed. Check client ID or amount.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid amount.");
        }
    }

    private void becomeClientGUI() {
        // Clerk -> Client is transition 1 (see nextState table)
        WarehouseContext.instance().changeState(1);
    }

    private void logoutGUI() {
        // Clerk -> Login is transition 0 (see nextState table)
        WarehouseContext.instance().changeState(0);
    }

    /* ======================  Action Listener ====================== */

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnLogout) {
            logoutGUI();
        } else if (src == btnAddClient) {
            addClientGUI();
        } else if (src == btnShowProducts) {
            showProductsGUI();
        } else if (src == btnShowClients) {
            showClientsGUI();
        } else if (src == btnShowClientsWithBalance) {
            showClientsWithBalanceGUI();
        } else if (src == btnRecordPayment) {
            recordPaymentGUI();
        } else if (src == btnBecomeClient) {
            becomeClientGUI();
        }
    }

    /* ======================  State Entry (GUI Build) ====================== */

    @Override
public void run() {
    frame = WarehouseContext.instance().getFrame();
    frame.setTitle("Clerk Menu");   // shows in OS title bar if decorations exist

    // ----- Clear and set top-level layout -----
    Container root = frame.getContentPane();
    root.removeAll();
    root.setLayout(new BorderLayout());

    // ===== Title label at top =====
    JLabel titleLabel = new JLabel("CLERK MENU", SwingConstants.CENTER);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
    root.add(titleLabel, BorderLayout.NORTH);

    // ===== Button panel in the center =====
    JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 15));
    GUIStyle.addDefaultPadding(buttonPanel);   // gives nice margins

    // Create buttons
    btnAddClient               = new JButton("Add Client");
    btnShowProducts            = new JButton("Show Products (qty & price)");
    btnShowClients             = new JButton("Show Clients");
    btnShowClientsWithBalance  = new JButton("Show Clients With Balance");
    btnRecordPayment           = new JButton("Record Client Payment");
    btnBecomeClient            = new JButton("Become Client");
    btnLogout                  = new JButton("Logout");

    // Listeners
    btnAddClient.addActionListener(this);
    btnShowProducts.addActionListener(this);
    btnShowClients.addActionListener(this);
    btnShowClientsWithBalance.addActionListener(this);
    btnRecordPayment.addActionListener(this);
    btnBecomeClient.addActionListener(this);
    btnLogout.addActionListener(this);

    // Style buttons
    GUIStyle.styleMainButton(btnAddClient);
    GUIStyle.styleMainButton(btnShowProducts);
    GUIStyle.styleMainButton(btnShowClients);
    GUIStyle.styleMainButton(btnShowClientsWithBalance);
    GUIStyle.styleMainButton(btnRecordPayment);
    GUIStyle.styleMainButton(btnBecomeClient);
    GUIStyle.styleMainButton(btnLogout);

    // Add buttons in the order you want (logout last)
    buttonPanel.add(btnAddClient);
    buttonPanel.add(btnShowProducts);
    buttonPanel.add(btnShowClients);
    buttonPanel.add(btnShowClientsWithBalance);
    buttonPanel.add(btnRecordPayment);
    buttonPanel.add(btnBecomeClient);
    buttonPanel.add(btnLogout);

    // Put button panel in the center of the frame
    root.add(buttonPanel, BorderLayout.CENTER);

    // Final refresh
    frame.setSize(800, 400);
    frame.revalidate();
    frame.repaint();
    frame.setVisible(true);
    frame.toFront();
    frame.requestFocus();
}
}
