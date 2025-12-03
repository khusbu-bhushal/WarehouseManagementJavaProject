import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class LoginMenuState extends WarehouseState implements ActionListener {

    private static LoginMenuState loginMenuState; // Singleton
    private static Warehouse warehouse;

    // Buttons (GUI)
    private JButton clientButton;
    private JButton clerkButton;
    private JButton managerButton;
    private JButton exitButton;

    // --- Constructor (private for singleton) ---
    private LoginMenuState() {
        super();
        warehouse = Warehouse.instance(); // same as before
    }

    // --- Singleton accessor ---
    public static LoginMenuState instance() {
        if (loginMenuState == null) {
            loginMenuState = new LoginMenuState();
        }
        return loginMenuState;
    }

    // ====== Helper methods to switch states (same logic you had) ======

    private void accessManagerMenu() {
        WarehouseContext ctx = WarehouseContext.instance();
        ctx.setLogin(WarehouseContext.IsManager);
        ctx.changeState(3);   // Manager state index
    }

    private void accessClerkMenu() {
        WarehouseContext ctx = WarehouseContext.instance();
        ctx.setLogin(WarehouseContext.IsClerk);
        ctx.changeState(2);   // Clerk state index
    }

    private void accessClientMenu() {
        WarehouseContext ctx = WarehouseContext.instance();
        ctx.setLogin(WarehouseContext.IsClient);
        ctx.changeState(1);   // Client state index
    }

    // ====== GUI  ======

    /** Clear everything from the frame when we leave this state. */
    private void clear(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.repaint();
    }

    /** Handle button clicks. */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == clientButton) {
            accessClientMenu();
        } else if (src == clerkButton) {
            accessClerkMenu();
        } else if (src == managerButton) {
            accessManagerMenu();
        } else if (src == exitButton) {
            WarehouseContext.instance().terminate();
        }
    }

    // ====== State entry: build the login GUI ======

    @Override
    public void run() {
    JFrame frame = WarehouseContext.instance().getFrame();

    // Set frame title (used if decorations are visible)
    frame.setTitle("Login Menu");

    // Clear old components
    Container root = frame.getContentPane();
    root.removeAll();

    // Top-level layout: title on top, buttons in the center
    root.setLayout(new BorderLayout());

    // ---- Title label ----
    JLabel titleLabel = new JLabel("LOGIN MENU", SwingConstants.CENTER);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
    root.add(titleLabel, BorderLayout.NORTH);

    // ---- Button panel ----
    JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 15));
    GUIStyle.addDefaultPadding(buttonPanel);  // optional nice padding

    // Create buttons
    clientButton   = new JButton("Login as Client");
    clerkButton    = new JButton("Login as Clerk");
    managerButton  = new JButton("Login as Manager");
    exitButton     = new JButton("Exit");

    // Style buttons
    GUIStyle.styleMainButton(clientButton);
    GUIStyle.styleMainButton(clerkButton);
    GUIStyle.styleMainButton(managerButton);
    GUIStyle.styleMainButton(exitButton);

    buttonPanel.add(clientButton);
    buttonPanel.add(clerkButton);
    buttonPanel.add(managerButton);
    buttonPanel.add(exitButton);

    // Add panel to frame
    root.add(buttonPanel, BorderLayout.CENTER);

    // Listeners
    clientButton.addActionListener(this);
    clerkButton.addActionListener(this);
    managerButton.addActionListener(this);
    exitButton.addActionListener(this);

    
    frame.revalidate();
    frame.repaint();
    frame.setVisible(true);
    frame.toFront();
    frame.requestFocus();
}
}
