import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class WarehouseContext {
  private static int currentState;
  private static Warehouse warehouse;
  private int currentUser;
  private String userID;
  private static WarehouseContext context;
  public static final int IsClient = 1;
  public static final int IsClerk = 2;
  public static final int IsManager = 3;
  private static WarehouseState[] states;
  private int[][] nextState;
  private static JFrame WarehouseFrame;
  private BufferedReader reader = new BufferedReader(
      new InputStreamReader(System.in));

  private WarehouseContext() {

      // nicer look & feel  If it fails, keep default.
    try {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (Exception e) {
        //ignoreee
    }

    states = new WarehouseState[5];
    states[4] = WishListMenuState.instance();
    states[3] = ManagerMenuState.instance();
    states[2] = ClerkMenuState.instance();
    states[1] = ClientMenuState.instance();
    states[0] = LoginMenuState.instance();

    currentState = 0;
    nextState = new int[5][5];
    nextState[0][0] = -1; nextState[0][1] = 1; nextState[0][2] = 2; nextState[0][3] = 3; nextState[0][4] = 0; // login
    nextState[1][0] = 0;  nextState[1][1] = 1; nextState[1][2] = 2; nextState[1][3] = 3; nextState[1][4] = 4; // client
    nextState[2][0] = 0;  nextState[2][1] = 1; nextState[2][2] = 2; nextState[2][3] = 3; nextState[2][4] = 2; // clerk
    nextState[3][0] = 0;  nextState[3][1] = 1; nextState[3][2] = 2; nextState[3][3] = 3; nextState[3][4] = 3; // manager
    nextState[4][0] = 0;  nextState[4][1] = 1; nextState[4][2] = 2; nextState[4][3] = 3; nextState[4][4] = 4; // wishlist

    // ---- GUI frame 

    JFrame.setDefaultLookAndFeelDecorated(true);


    WarehouseFrame = new JFrame("Warehouse FSM GUI");
    
    WarehouseFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    // a bit smaller, still wide enough for full button text
    WarehouseFrame.setSize(500, 300);

    // center the window on the VNC screen
    WarehouseFrame.setLocationRelativeTo(null);

    WarehouseFrame.setVisible(true);
    

  }

  // Prompts the user and retrieves their input (still used by text states)
  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }

  // Prompts the user with yes or no and returns bool
  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }

  public JFrame getFrame() {
    return WarehouseFrame;
  }

  public void setLogin(int code) {
    currentUser = code;
  }

  public void setUser(String uID) {
    userID = uID;
  }

  public int getLogin() {
    return currentUser;
  }

  public String getUser() {
    return userID;
  }

  // Initiates the change of state based on current and desired state
  public void changeState(int transition) {
    currentState = nextState[currentState][transition];
    if (currentState == -2) {
      System.out.println("Error has occurred");
      terminate();
    }
    if (currentState == -1) {
      terminate();
    }
    states[currentState].run();
  }

  // Exits the program
  public void terminate() {
    System.exit(0);
  }

  // Singleton
  public static WarehouseContext instance() {
    if (context == null) {
      System.out.println("calling constructor");
      context = new WarehouseContext();
    }
    return context;
  }

  public void process() {
    states[currentState].run();
  }

  public static void main(String[] args) {
    WarehouseContext.instance().process();
  }
}
