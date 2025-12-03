import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class WishListMenuState extends WarehouseState implements ActionListener {

    private static WishListMenuState instance;
    private static Warehouse warehouse;

    // GUI
    private JFrame frame;
    private JButton btnAddItem;
    private JButton btnDisplayWishlist;
    private JButton btnPlaceOrder;
    private JButton btnBackToClient;
    private JButton btnLogout;

    private WishListMenuState() {
        super();
        warehouse = Warehouse.instance();
    }

    public static WishListMenuState instance() {
        if (instance == null) {
            instance = new WishListMenuState();
        }
        return instance;
    }

    /* ================== Helper methods (GUI versions) ================== */

    // 0. Add item to wishlist
    private void addToWishListGUI() {
        String clientId = JOptionPane.showInputDialog(frame, "Enter Client ID:");
        if (clientId == null || clientId.trim().isEmpty()) return;

        boolean more = true;
        while (more) {
            String prodID = JOptionPane.showInputDialog(frame, "Enter Product ID:");
            if (prodID == null || prodID.trim().isEmpty()) break;

            String qtyStr = JOptionPane.showInputDialog(frame, "Enter Product Quantity:");
            if (qtyStr == null || qtyStr.trim().isEmpty()) break;

            try {
                int quantity = Integer.parseInt(qtyStr.trim());
                warehouse.addToWishList(clientId.trim(), prodID.trim(), quantity);
                int again = JOptionPane.showConfirmDialog(
                        frame,
                        "Item added. Add another item?",
                        "Wishlist",
                        JOptionPane.YES_NO_OPTION
                );
                if (again != JOptionPane.YES_OPTION) {
                    more = false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid quantity.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                more = false;
            }
        }
    }

    // 1. Display wishlist
    // 1. Display wishlist
private void displayWishlistGUI() {
    String clientId = JOptionPane.showInputDialog(frame, "Enter client ID:");
    if (clientId == null || clientId.trim().isEmpty()) return;

    WishList wishListObj = warehouse.getWishList(clientId.trim());

    if (wishListObj == null) {
        JOptionPane.showMessageDialog(
            frame,
            "No wishlist found for client " + clientId,
            "Wishlist",
            JOptionPane.INFORMATION_MESSAGE
        );
        return;
    }

    // Original raw text from toString()
    String raw = wishListObj.toString();
    // Example: WishList{clientId='C0001', items=[P0001 - Mouse x 5, P0003 - paper x 5]}

    StringBuilder sb = new StringBuilder();
    sb.append("Wishlist for client ").append(clientId).append("\n\n");

    int itemsIndex = raw.indexOf("items=[");
    if (itemsIndex != -1) {
        int start = itemsIndex + "items=[".length();
        int end = raw.lastIndexOf("]");
        if (end > start) {
            String itemsPart = raw.substring(start, end);  // "P0001 - Mouse x 5, P0003 - paper x 5"
            String[] items = itemsPart.split(", ");

            for (String item : items) {
                sb.append(item).append("\n");
            }
        } else {
            sb.append("(no items found)\n");
        }
    } else {
        // Fallback: show the raw string if format is unexpected
        sb.append(raw);
    }

    JOptionPane.showMessageDialog(
        frame,
        sb.toString(),
        "Wishlist",
        JOptionPane.INFORMATION_MESSAGE
    );
}


    // helper: check if client exists
    private boolean clientExists(String clientId) {
        for (Client c : warehouse.listClients()) {
            if (c.getId().equalsIgnoreCase(clientId)) return true;
        }
        return false;
    }

    // 2. Place order from wishlist (and optional extra order)
    private void placeOrderGUI() {
        // prompt for a valid client ID, or cancel
        String clientId = null;
        while (true) {
            String input = JOptionPane.showInputDialog(frame, "Enter client ID:");
            if (input == null) {
                return; // cancel
            }
            input = input.trim();
            if (input.isEmpty()) continue;

            if (clientExists(input)) {
                clientId = input;
                break;
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Client not found. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        int orderWishlist = JOptionPane.showConfirmDialog(
                frame,
                "Order all items on client " + clientId + "'s wishlist?",
                "Place Order",
                JOptionPane.YES_NO_OPTION
        );
        if (orderWishlist == JOptionPane.YES_OPTION) {
            warehouse.orderWishlist(clientId);
            JOptionPane.showMessageDialog(frame,
                    "Wishlist order placed.",
                    "Place Order",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        int extra = JOptionPane.showConfirmDialog(
                frame,
                "Place an additional order for a single product?",
                "Additional Order",
                JOptionPane.YES_NO_OPTION
        );
        if (extra == JOptionPane.YES_OPTION) {
            String productId = JOptionPane.showInputDialog(frame, "Enter product ID:");
            if (productId == null || productId.trim().isEmpty()) return;

            String qtyStr = JOptionPane.showInputDialog(frame, "Enter product quantity:");
            if (qtyStr == null || qtyStr.trim().isEmpty()) return;

            try {
                int qty = Integer.parseInt(qtyStr.trim());
                Map<String, Integer> orderItems = new HashMap<>();
                orderItems.put(productId.trim(), qty);

                warehouse.placeOrder(clientId, orderItems);
                JOptionPane.showMessageDialog(frame,
                        "Order placed for product " + productId + " x" + qty + ".",
                        "Place Order",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid quantity.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 3. Back to Client Menu
    private void backToClientGUI() {
        WarehouseContext.instance().changeState(1);  // 1 = ClientMenuState
    }

    // 4. Logout (same logic you had)
    private void logoutGUI() {
        WarehouseContext ctx = WarehouseContext.instance();
        int loginType = ctx.getLogin();
        int transition;

        if (loginType == WarehouseContext.IsClient) {
            transition = 0;   // back to Login
        } else {
            transition = 2;   // back to Clerk
        }

        ctx.changeState(transition);
    }

    /* ================== ActionListener ================== */

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnAddItem) {
            addToWishListGUI();
        } else if (src == btnDisplayWishlist) {
            displayWishlistGUI();
        } else if (src == btnPlaceOrder) {
            placeOrderGUI();
        } else if (src == btnBackToClient) {
            backToClientGUI();
        } else if (src == btnLogout) {
            logoutGUI();
        }
    }

    /* ================== State entry (build GUI) ================== */

    @Override
public void run() {
    frame = WarehouseContext.instance().getFrame();
    frame.setTitle("Wishlist Menu");   // window title (if decorations exist)

    // ----- Root container -----
    Container root = frame.getContentPane();
    root.removeAll();
    root.setLayout(new BorderLayout());

    // ===== Title label at top =====
    JLabel titleLabel = new JLabel("WISHLIST MENU", SwingConstants.CENTER);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
    root.add(titleLabel, BorderLayout.NORTH);

    // ===== Button panel in center =====
    JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 15));
    GUIStyle.addDefaultPadding(buttonPanel);   // same padding as other menus

    // Create buttons
    btnAddItem        = new JButton("Add item to Wishlist");
    btnDisplayWishlist = new JButton("Display Wishlist");
    btnPlaceOrder     = new JButton("Place Order from Wishlist");
    btnBackToClient   = new JButton("Back to Client Menu");
    btnLogout         = new JButton("Logout");

    // Listeners
    btnAddItem.addActionListener(this);
    btnDisplayWishlist.addActionListener(this);
    btnPlaceOrder.addActionListener(this);
    btnBackToClient.addActionListener(this);
    btnLogout.addActionListener(this);

    // Style buttons
    GUIStyle.styleMainButton(btnAddItem);
    GUIStyle.styleMainButton(btnDisplayWishlist);
    GUIStyle.styleMainButton(btnPlaceOrder);
    GUIStyle.styleMainButton(btnBackToClient);
    GUIStyle.styleMainButton(btnLogout);

    // Add buttons in desired order
    buttonPanel.add(btnAddItem);
    buttonPanel.add(btnDisplayWishlist);
    buttonPanel.add(btnPlaceOrder);
    buttonPanel.add(btnBackToClient);
    buttonPanel.add(btnLogout);

    // Add panel to center
    root.add(buttonPanel, BorderLayout.CENTER);

    // Final refresh
    frame.setSize(800, 400);   // keep consistent with other menus
    frame.revalidate();
    frame.repaint();
    frame.setVisible(true);
    frame.toFront();
    frame.requestFocus();
}
}
