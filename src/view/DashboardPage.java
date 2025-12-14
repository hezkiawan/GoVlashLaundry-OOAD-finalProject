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
    
    // Navigation buttons
    private Button logoutBtn;
    
    // Role specific buttons
    // admin
    private Button manageServiceBtn; 
    private Button manageEmployeeBtn; 
    private Button viewTransBtn;      
    
    // Customer
    private Button viewServicesBtn;    
    private Button myHistoryBtn;       
    private Button myNotificationsBtn; 
    
    // Receptionist
    private Button pendingTransBtn; 
    
    // Laundry staff
    private Button myTasksBtn;
    
    // Current User
    private User currentUser;

    public DashboardPage() {
        currentUser = UserSession.getInstance();
        initComp();
        initPos();
        setupSidebarByRole();
        loadDefaultView();
    }

    private void initComp() {
        bp = new BorderPane();
        sidebar = new VBox(10);
        
        welcomeLbl = new Label("Welcome, " + currentUser.getName());
        welcomeLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
        
        // Common buttons
        logoutBtn = new Button("Log Out");
        
        // Admin buttons
        manageServiceBtn = new Button("Manage Services");
        manageEmployeeBtn = new Button("Manage Employees");
        viewTransBtn = new Button("View Transactions"); 
        
        // Customer buttons
        viewServicesBtn = new Button("Order Service");
        myHistoryBtn = new Button("My Transaction History");
        myNotificationsBtn = new Button("Notifications");
        
        // Receptionist buttons
        pendingTransBtn = new Button("Pending Transactions");
        
        // Staff Buttons
        myTasksBtn = new Button("My Tasks");
        
        // Style all buttons to fill width
        styleButton(logoutBtn);
        styleButton(manageServiceBtn);
        styleButton(manageEmployeeBtn);
        styleButton(viewTransBtn);
        styleButton(viewServicesBtn);
        styleButton(myHistoryBtn);
        styleButton(myNotificationsBtn);
        styleButton(pendingTransBtn);
        styleButton(myTasksBtn);
        
        // Register events
        logoutBtn.setOnAction(this);
        manageServiceBtn.setOnAction(this);
        manageEmployeeBtn.setOnAction(this);
        viewTransBtn.setOnAction(this); 
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
    }
    
    private void setupSidebarByRole() {
        sidebar.getChildren().clear();
        sidebar.getChildren().add(welcomeLbl);
        sidebar.getChildren().add(new Separator());
        
        String role = currentUser.getRole();
        
        // DYNAMIC CONTENT BASED ON ROLE
        if (role.equals("Admin")) {
            //Admin manages services, employees, and views transactions
            sidebar.getChildren().addAll(manageServiceBtn, manageEmployeeBtn, viewTransBtn);
            
        } else if (role.equals("Customer")) {
            // View services, history, notifications
            sidebar.getChildren().addAll(viewServicesBtn, myHistoryBtn, myNotificationsBtn);
            
        } else if (role.equals("Receptionist")) {
            // Viiew pending transactions
            sidebar.getChildren().add(pendingTransBtn);
            
        } else if (role.equals("Laundry Staff")) {
            // View assigned orders
            sidebar.getChildren().add(myTasksBtn);
        }
        
        sidebar.getChildren().add(new Separator());
        sidebar.getChildren().add(logoutBtn);
    }
    
    // Helper method to decide which view to show user first
    private void loadDefaultView() {
        String role = currentUser.getRole();
        
        if (role.equals("Admin")) {
            bp.setCenter(new ManageServiceView()); 
        } else if (role.equals("Customer")) {
            bp.setCenter(new BuyServiceView());
        } else if (role.equals("Receptionist")) {
            bp.setCenter(new ReceptionistView());
        } else if (role.equals("Laundry Staff")) {
            bp.setCenter(new LaundryStaffView());
        }
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
        
        // ADMIN VIEWS
        else if (e.getSource() == manageServiceBtn) {
            bp.setCenter(new ManageServiceView()); 
        }
        else if (e.getSource() == manageEmployeeBtn) {
            bp.setCenter(new ManageEmployeeView());
        }
        else if (e.getSource() == viewTransBtn) {
            bp.setCenter(new AdminTransactionView());
        }

        // CUSTOMER VIEWS
        else if (e.getSource() == viewServicesBtn) {
            bp.setCenter(new BuyServiceView());
        }
        else if (e.getSource() == myHistoryBtn) {
            bp.setCenter(new CustomerHistoryView());
        }
        else if (e.getSource() == myNotificationsBtn) {
            bp.setCenter(new CustomerNotificationView());
        }
        
        // RECEPTIONIST VIEWS
        else if (e.getSource() == pendingTransBtn) {
            bp.setCenter(new ReceptionistView());
        }
        // STAFF VIEWS
        else if (e.getSource() == myTasksBtn) {
            bp.setCenter(new LaundryStaffView());
        }
    }
}