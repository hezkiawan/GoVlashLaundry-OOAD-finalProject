package view;

import controller.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.Main;
import main.UserSession;
import model.User;

public class LoginPage implements EventHandler<ActionEvent> {

    // Components
    private Scene scene;
    private BorderPane bp;
    private GridPane grid;
    private VBox mainLayout;
    
    private Label titleLbl, emailLbl, passLbl;
    private TextField emailField;
    private PasswordField passField;
    private Button loginBtn, registerBtn;
    
    private Alert errorAlert;
    
    // Controller
    private UserController userController = new UserController();

    public LoginPage() {
        initComp();
        initPos();
    }

    private void initComp() {
        bp = new BorderPane();
        grid = new GridPane();
        mainLayout = new VBox(20);
        
        // Labels
        titleLbl = new Label("GoVlash Laundry Login");
        titleLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        emailLbl = new Label("Email:");
        passLbl = new Label("Password:");
        
        // Inputs
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        
        passField = new PasswordField();
        passField.setPromptText("Enter your password");
        
        // Buttons
        loginBtn = new Button("Login");
        loginBtn.setMinWidth(100);
        loginBtn.setOnAction(this); // Register Event
        
        registerBtn = new Button("Register New Account");
        registerBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: blue; -fx-underline: true;");
        registerBtn.setOnAction(this); 
        
        // Alert
        errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Login Error");
        
        // Scene setup
        scene = new Scene(bp, 400, 350);
    }

    private void initPos() {
        // Grid setup
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        // Add to grid
        grid.add(emailLbl, 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(passLbl, 0, 1);
        grid.add(passField, 1, 1);
        
        // Add grid and buttons to main VBox
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(titleLbl, grid, loginBtn, registerBtn);
        
        bp.setCenter(mainLayout);
    }
    
    public Scene getScene() {
        return scene;
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            handleLogin();
        } else if (e.getSource() == registerBtn) {
            // Navigate to register page
            Main.setScene(new RegisterPage().getScene());
        }
    }
    
    private void handleLogin() {
        String email = emailField.getText();
        String pass = passField.getText();
        
        // 1. Call controller
        User user = userController.login(email, pass);
        
        if (user != null) {
            // 2. Success: save session
            UserSession.setInstance(user);
            
            // 3. Navigate to dashboard 
            // make  DashboardPage that checks UserSession to build the sidebar
            Main.setScene(new DashboardPage().getScene()); 
            
        } else {
            // 4. Fail
            errorAlert.setContentText("Invalid Email or Password");
            errorAlert.show();
        }
    }
}