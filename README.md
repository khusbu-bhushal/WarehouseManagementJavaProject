### This is a multi-stage Java project designed and implemented using Object-Oriented Programming, UML modeling, and a Finite State Machine (FSM).

*The system mimics core features of a warehouse environment, including clients, products, wishlists, transactions, and inventory operations.*
  
- Developed collaboratively as part of a four-person team.

- Technologies Used: Java, OOP, UML, State Design Pattern, Finite State Machine (FSM), SOLID Principles.

### 👩‍💻 My Contributions

- Designed the `WishList.java` backend based on a use case I wrote from initial requirements.
- Developed the `WishListMenuState` on top of the WishList backend.
- Implemented the `ManagerMenuState` logic and menu flow.
- Refined and reorganized class designs for consistency and clarity.
- Ensured OOP principles and LSP consistency.
- Improved the logic, reliability, and flow of state transitions within the FSM.

The test flow is outlined in testprocedure.txt to ensure that the program runs successfully. 

This project was originally developed and executed in GitHub Codespaces using instructions provided in the class to enable GUI support. Codespaces does not provide GUI functionality by default, so specific additional setup steps were required to run the program.

The full setup included:

- Installing and configuring a VNC server

- Opening a web socket connection

- Setting DISPLAY=:1 in the terminal before running Java GUI code

These steps were part of the course environment and are not included in this public repository, since the project can still be reviewed through its Java source code, architecture, and state machine implementation.
If you wish to run the GUI locally, you can clone the repository and run the Java files using any standard Java-supported IDE (IntelliJ, VS Code with Java extensions, or Eclipse).

How to Run

- Clone the repository:

  git clone https://github.com/your-repo/WarehouseManagementJavaProject.git

- Open the project in any Java-supported IDE (IntelliJ, Eclipse, VS Code with Java extensions).

- Run WarehouseContext.java or GUIStyle.java to launch the system.
If running outside an IDE, ensure Java 8+ is installed.
