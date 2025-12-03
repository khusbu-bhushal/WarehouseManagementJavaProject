import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientMenuState extends WarehouseState implements ActionListener {

    private static Warehouse warehouse;
    private static ClientMenuState clientMenuState; // singleton

    // GUI components
    private JFrame frame;
    private JButton btnShowClients;
    private JButton btnShowProducts;
    private JButton btnShowTransactions;
    private JButton btnWishlistOps;
    private JButton btnLogout;

    // For options / readability (event numbers are still handled in WarehouseContext)
    private static final int wishlistMenu = 3;

    private ClientMenuState() {
        super();
        warehouse = Warehouse.instance(); // global warehouse
    }

    public static ClientMenuState instance() {
        if (clientMenuState == null) {
            clientMenuState = new ClientMenuState();
        }
        return clientMenuState;
    }

    /* ---------- Helper methods that now use dialogs instead of println ---------- */

    private void showClientDetailsGUI() {
        StringBuilder sb = new StringBuilder();
        sb.append("ClientID | Name | Address | Balance\n");
        for (Client c : warehouse.listClients()) {
            sb.append(c.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString(),
                "All Clients", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showProductsGUI() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProductID | Name | Quantity | Price\n");
        for (Product p : warehouse.listProducts()) {
            sb.append(p.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString(),
                "All Products", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showTransactionsGUI() {
        String clientId = JOptionPane.showInputDialog(
                frame, "Enter client ID to view wishlist/transactions:");
        if (clientId == null || clientId.trim().isEmpty()) {
            return; // user cancelled
        }

        Object wishListObj = warehouse.getWishList(clientId.trim());
        if (wishListObj == null) {
            JOptionPane.showMessageDialog(frame,
                    "No wishlist / transactions found for client " + clientId,
                    "No Data", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Uses WishList.toString() or whatever you defined
            String text = String.valueOf(wishListObj);
            JOptionPane.showMessageDialog(frame, text,
                    "Wishlist / Transactions for " + clientId,
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void goToWishlistMenu() {
        // Event 4 = ToWishList (we set that up in WarehouseContext)
        WarehouseContext.instance().changeState(4);
    }

    private void logout() {
        WarehouseContext ctx = WarehouseContext.instance();
        int loginType = ctx.getLogin();

        int transition;
        if (loginType == WarehouseContext.IsClient) {
            // logged in directly as client → back to Login
            transition = 0;
        } else {
            // became client from Clerk → back to Clerk
            transition = 2;
        }
        ctx.changeState(transition);
    }

    /* ---------- ActionListener ---------- */

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnShowClients) {
            showClientDetailsGUI();
        } else if (src == btnShowProducts) {
            showProductsGUI();
        } else if (src == btnShowTransactions) {
            showTransactionsGUI();
        } else if (src == btnWishlistOps) {
            goToWishlistMenu();
        } else if (src == btnLogout) {
            logout();
        }
    }

    /* ---------- Run: build the GUI for this state ---------- */

    @Override
public void run() {
    // Shared frame
    frame = WarehouseContext.instance().getFrame();
    frame.setTitle("Client Menu");   // OS/window title bar (if shown)

    // ----- Root layout -----
    Container root = frame.getContentPane();
    root.removeAll();
    root.setLayout(new BorderLayout());

    // ===== Title label at the top =====
    JLabel titleLabel = new JLabel("CLIENT MENU", SwingConstants.CENTER);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
    root.add(titleLabel, BorderLayout.NORTH);

    // ===== Button panel in the center =====
    JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 15));
    GUIStyle.addDefaultPadding(buttonPanel);   // nice margins around the buttons

    // create buttons
    btnShowClients       = new JButton("Show Client Details");
    btnShowProducts      = new JButton("Show Products (price only)");
    btnShowTransactions  = new JButton("Show Client Transactions");
    btnWishlistOps       = new JButton("Wishlist operations");
    btnLogout            = new JButton("Logout");

    // listeners
    btnShowClients.addActionListener(this);
    btnShowProducts.addActionListener(this);
    btnShowTransactions.addActionListener(this);
    btnWishlistOps.addActionListener(this);
    btnLogout.addActionListener(this);

    // style buttons
    GUIStyle.styleMainButton(btnShowClients);
    GUIStyle.styleMainButton(btnShowProducts);
    GUIStyle.styleMainButton(btnShowTransactions);
    GUIStyle.styleMainButton(btnWishlistOps);
    GUIStyle.styleMainButton(btnLogout);

    // add buttons to panel (in the order you want)
    buttonPanel.add(btnShowClients);
    buttonPanel.add(btnShowProducts);
    buttonPanel.add(btnShowTransactions);
    buttonPanel.add(btnWishlistOps);
    buttonPanel.add(btnLogout);

    // put button panel in the center
    root.add(buttonPanel, BorderLayout.CENTER);

    // refresh frame
    frame.setSize(800, 400);   // same size as others if you like
    frame.revalidate();
    frame.repaint();
    frame.setVisible(true);
    frame.toFront();
    frame.requestFocus();
}
}
