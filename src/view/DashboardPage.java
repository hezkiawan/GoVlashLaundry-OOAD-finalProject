package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.Main;
import main.UserSession;
import model.User;

public class DashboardPage implements EventHandler<ActionEvent> {

    // Components
    private Scene scene;
    private BorderPane bp;
    private VBox sidebar;
    private Label welcomeLbl;
    
    // Navigation Buttons
    private Button homeBtn, logoutBtn;
    
    // Role-Specific Buttons
    private Button manageServiceBtn; // Admin
    private Button manageEmployeeBtn; // Admin
    private Button viewTransBtn;      // Admin (View Transactions & Send Notif)
    
    private Button viewServicesBtn;    // Customer (Buy)
    private Button myHistoryBtn;       // Customer
    private Button myNotificationsBtn; // Customer
    
    private Button pendingTransBtn;    // Receptionist
    private Button myTasksBtn;         // Laundry Staff
    
    // Current User
    private User currentUser;

    public DashboardPage() {
        currentUser = UserSession.getInstance();
        initComp();
        initPos();
        setupSidebarByRole();
    }

    private void initComp() {
        bp = new BorderPane();
        sidebar = new VBox(10); // Spacing 10
        
        welcomeLbl = new Label("Welcome, " + currentUser.getName());
        welcomeLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
        
        // Common Buttons
        homeBtn = new Button("Home");
        logoutBtn = new Button("Logout");
        
        // Admin Buttons
        manageServiceBtn = new Button("Manage Services");
        manageEmployeeBtn = new Button("Manage Employees");
        viewTransBtn = new Button("View Transactions"); // New Button
        
        // Customer Buttons
        viewServicesBtn = new Button("Order Service");
        myHistoryBtn = new Button("My Transaction History");
        myNotificationsBtn = new Button("Notifications");
        
        // Receptionist Buttons
        pendingTransBtn = new Button("Pending Transactions");
        
        // Staff Buttons
        myTasksBtn = new Button("My Tasks");
        
        // Style all buttons to fill width
        styleButton(homeBtn);
        styleButton(logoutBtn);
        styleButton(manageServiceBtn);
        styleButton(manageEmployeeBtn);
        styleButton(viewTransBtn);
        styleButton(viewServicesBtn);
        styleButton(myHistoryBtn);
        styleButton(myNotificationsBtn);
        styleButton(pendingTransBtn);
        styleButton(myTasksBtn);
        
        // Register Events
        homeBtn.setOnAction(this);
        logoutBtn.setOnAction(this);
        manageServiceBtn.setOnAction(this);
        manageEmployeeBtn.setOnAction(this);
        viewTransBtn.setOnAction(this); // Register Event
        viewServicesBtn.setOnAction(this);
        myHistoryBtn.setOnAction(this);
        myNotificationsBtn.setOnAction(this);
        pendingTransBtn.setOnAction(this);
        myTasksBtn.setOnAction(this);
        
        scene = new Scene(bp, 900, 600);
    }
    
    private void styleButton(Button b) {
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER-LEFT;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-alignment: CENTER-LEFT;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER-LEFT;"));
    }

    private void initPos() {
        // Sidebar Style
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #333;");
        sidebar.setPrefWidth(200);
        
        bp.setLeft(sidebar);
        
        // Set Default Center
        Label homeLbl = new Label("Welcome to GoVlash Laundry System");
        homeLbl.setStyle("-fx-font-size: 24px;");
        bp.setCenter(homeLbl);
    }
    
    private void setupSidebarByRole() {
        sidebar.getChildren().clear();
        sidebar.getChildren().add(welcomeLbl);
        sidebar.getChildren().add(new Separator());
        sidebar.getChildren().add(homeBtn);
        
        String role = currentUser.getRole();
        
        // DYNAMIC CONTENT BASED ON ROLE
        if (role.equals("Admin")) {
            // Source 10, 14, 41: Admin manages services, employees, and views transactions
            sidebar.getChildren().addAll(manageServiceBtn, manageEmployeeBtn, viewTransBtn);
            
        } else if (role.equals("Customer")) {
            // Source 11, 36, 24: View services, history, notifications
            sidebar.getChildren().addAll(viewServicesBtn, myHistoryBtn, myNotificationsBtn);
            
        } else if (role.equals("Receptionist")) {
            // Source 42: View pending transactions
            sidebar.getChildren().add(pendingTransBtn);
            
        } else if (role.equals("Laundry Staff")) {
            // Source 43: View assigned orders
            sidebar.getChildren().add(myTasksBtn);
        }
        
        sidebar.getChildren().add(new Separator());
        sidebar.getChildren().add(logoutBtn);
    }
    
    public Scene getScene() {
        return scene;
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == logoutBtn) {
            UserSession.clear();
            Main.setScene(new LoginPage().getScene());
        } 
        else if (e.getSource() == homeBtn) {
            bp.setCenter(new Label("Home Page"));
        }
        
        // --- ADMIN VIEWS ---
        else if (e.getSource() == manageServiceBtn) {
            bp.setCenter(new ManageServiceView()); 
        }
        else if (e.getSource() == manageEmployeeBtn) {
            bp.setCenter(new ManageEmployeeView());
        }
        else if (e.getSource() == viewTransBtn) {
            // Link to AdminTransactionView (includes Send Notification)
            bp.setCenter(new AdminTransactionView());
        }

        // --- CUSTOMER VIEWS ---
        else if (e.getSource() == viewServicesBtn) {
            bp.setCenter(new BuyServiceView());
        }
        else if (e.getSource() == myHistoryBtn) {
            bp.setCenter(new CustomerHistoryView());
        }
        else if (e.getSource() == myNotificationsBtn) {
            // Link to CustomerNotificationView (Read/Delete Notif)
            bp.setCenter(new CustomerNotificationView());
        }
        
        // --- STAFF VIEWS ---
        else if (e.getSource() == pendingTransBtn) {
            bp.setCenter(new ReceptionistView());
        }
        else if (e.getSource() == myTasksBtn) {
            bp.setCenter(new LaundryStaffView());
        }
    }
}