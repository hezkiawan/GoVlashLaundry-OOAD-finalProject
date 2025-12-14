package view;

import java.time.LocalDate;

import controller.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Main;

public class RegisterPage implements EventHandler<ActionEvent> {

    // Components
    private Scene scene;
    private BorderPane bp;
    private GridPane grid;
    private VBox mainLayout;
    
    private Label titleLbl, nameLbl, emailLbl, passLbl, confPassLbl, genderLbl, dobLbl;
    private TextField nameField, emailField;
    private PasswordField passField, confPassField;
    private RadioButton maleRb, femaleRb;
    private ToggleGroup genderGroup;
    private DatePicker dobPicker;
    
    private Button registerBtn, backBtn;
    
    // Controller
    private UserController userController = new UserController();
    private Alert alert;

    public RegisterPage() {
        initComp();
        initPos();
    }

    private void initComp() {
        bp = new BorderPane();
        grid = new GridPane();
        mainLayout = new VBox(20);
        
        titleLbl = new Label("Register New Customer");
        titleLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        nameLbl = new Label("Username:");
        emailLbl = new Label("Email:");
        passLbl = new Label("Password:");
        confPassLbl = new Label("Confirm Password:");
        genderLbl = new Label("Gender:");
        dobLbl = new Label("Date of Birth:");
        
        nameField = new TextField();
        emailField = new TextField();
        passField = new PasswordField();
        confPassField = new PasswordField();
        
        // Gender using radio buttons
        maleRb = new RadioButton("Male");
        femaleRb = new RadioButton("Female");
        genderGroup = new ToggleGroup();
        maleRb.setToggleGroup(genderGroup);
        femaleRb.setToggleGroup(genderGroup);
        
        // Date picker
        dobPicker = new DatePicker();
        
        registerBtn = new Button("Register");
        registerBtn.setOnAction(this);
        
        backBtn = new Button("Back to Login");
        backBtn.setOnAction(this);
        
        alert = new Alert(AlertType.NONE); 
        
        scene = new Scene(bp, 500, 600);
    }

    private void initPos() {
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        
        // Add components to grid
        grid.add(nameLbl, 0, 0);
        grid.add(nameField, 1, 0);
        
        grid.add(emailLbl, 0, 1);
        grid.add(emailField, 1, 1);
        
        grid.add(passLbl, 0, 2);
        grid.add(passField, 1, 2);
        
        grid.add(confPassLbl, 0, 3);
        grid.add(confPassField, 1, 3);
        
        grid.add(genderLbl, 0, 4);
        HBox genderBox = new HBox(10);
        genderBox.getChildren().addAll(maleRb, femaleRb);
        grid.add(genderBox, 1, 4);
        
        grid.add(dobLbl, 0, 5);
        grid.add(dobPicker, 1, 5);
        
        grid.add(registerBtn, 0, 6);
        grid.add(backBtn, 1, 6);
        
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(titleLbl, grid);
        
        bp.setCenter(mainLayout);
    }
    
    public Scene getScene() {
        return scene;
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == backBtn) {
            Main.setScene(new LoginPage().getScene());
        } else if (e.getSource() == registerBtn) {
            handleRegister();
        }
    }
    
    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String pass = passField.getText();
        String confPass = confPassField.getText();
        LocalDate dob = dobPicker.getValue();
        
        // Get gender
        String gender = null;
        if (maleRb.isSelected()) gender = "Male";
        else if (femaleRb.isSelected()) gender = "Female";
        
        // Register customer
        String result = userController.register(name, email, pass, confPass, gender, dob, "Customer");
        
        if (result.equals("Success")) {
            alert.setAlertType(AlertType.INFORMATION);
            alert.setContentText("Registration Successful! Please Login.");
            alert.showAndWait();
            
            // Redirect to Login
            Main.setScene(new LoginPage().getScene());
        } else {
            alert.setAlertType(AlertType.ERROR);
            alert.setContentText(result);
            alert.show();
        }
    }
}