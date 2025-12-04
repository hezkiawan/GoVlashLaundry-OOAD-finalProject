package view;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Vector;

import controller.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Employee;

public class ManageEmployeeView extends BorderPane implements EventHandler<ActionEvent> {

    // Components
    private VBox mainLayout;
    private GridPane formGrid;
    private Label titleLbl, nameLbl, emailLbl, passLbl, confPassLbl, genderLbl, dobLbl, roleLbl;
    
    private TextField nameField, emailField;
    private PasswordField passField, confPassField;
    private RadioButton maleRb, femaleRb;
    private ToggleGroup genderGroup;
    private DatePicker dobPicker;
    private ComboBox<String> roleCombo;
    
    private Button addBtn, clearBtn;
    private TableView<Employee> table;
    private Alert alert;

    // Controller
    private UserController userController = new UserController();
    private Vector<Employee> employees;

    public ManageEmployeeView() {
        initComp();
        initPos();
        refreshTable();
    }

    private void initComp() {
        mainLayout = new VBox(20);
        formGrid = new GridPane();
        
        titleLbl = new Label("Manage Employees");
        titleLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Form Labels
        nameLbl = new Label("Name:");
        emailLbl = new Label("Email:");
        passLbl = new Label("Password:");
        confPassLbl = new Label("Confirm Pass:");
        genderLbl = new Label("Gender:");
        dobLbl = new Label("Date of Birth:");
        roleLbl = new Label("Role:");

        // Form Inputs
        nameField = new TextField();
        emailField = new TextField();
        emailField.setPromptText("must end with @govlash.com");
        
        passField = new PasswordField();
        confPassField = new PasswordField();
        
        maleRb = new RadioButton("Male");
        femaleRb = new RadioButton("Female");
        genderGroup = new ToggleGroup();
        maleRb.setToggleGroup(genderGroup);
        femaleRb.setToggleGroup(genderGroup);
        maleRb.setSelected(true); // Default
        
        dobPicker = new DatePicker();
        
        // Role Combo
        roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Laundry Staff", "Receptionist");
        roleCombo.getSelectionModel().selectFirst();

        // Buttons
        addBtn = new Button("Add Employee");
        clearBtn = new Button("Clear Form");

        addBtn.setOnAction(this);
        clearBtn.setOnAction(this);

        alert = new Alert(AlertType.NONE);

        // Table Setup
        table = new TableView<>();
        setupTableColumns();
    }

    private void setupTableColumns() {
        TableColumn<Employee, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Employee, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(150);
        
        TableColumn<Employee, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setMinWidth(200);
        
        TableColumn<Employee, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        TableColumn<Employee, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        
        TableColumn<Employee, Date> dobCol = new TableColumn<>("DOB");
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
        
        table.getColumns().add(idCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(emailCol);
        table.getColumns().add(roleCol);
        table.getColumns().add(genderCol);
        table.getColumns().add(dobCol);
    }

    private void initPos() {
        // Form Layout
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);
        
        formGrid.add(nameLbl, 0, 0);
        formGrid.add(nameField, 1, 0);
        
        formGrid.add(emailLbl, 0, 1);
        formGrid.add(emailField, 1, 1);
        
        formGrid.add(passLbl, 0, 2);
        formGrid.add(passField, 1, 2);
        
        formGrid.add(confPassLbl, 0, 3);
        formGrid.add(confPassField, 1, 3);
        
        formGrid.add(genderLbl, 0, 4);
        HBox genderBox = new HBox(10);
        genderBox.getChildren().addAll(maleRb, femaleRb);
        formGrid.add(genderBox, 1, 4);
        
        formGrid.add(dobLbl, 0, 5);
        formGrid.add(dobPicker, 1, 5);
        
        formGrid.add(roleLbl, 0, 6);
        formGrid.add(roleCombo, 1, 6);

        // Button Layout
        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(addBtn, clearBtn);

        // Main Layout
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(titleLbl, table, formGrid, btnBox);

        this.setCenter(mainLayout);
    }

    private void refreshTable() {
        // Source 14: View all employees
        employees = userController.getAllEmployees();
        ObservableList<Employee> empObs = FXCollections.observableArrayList(employees);
        table.setItems(empObs);
    }
    
    private void clearForm() {
        nameField.clear();
        emailField.clear();
        passField.clear();
        confPassField.clear();
        maleRb.setSelected(true);
        dobPicker.setValue(null);
        roleCombo.getSelectionModel().selectFirst();
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == clearBtn) {
            clearForm();
        } 
        else if (e.getSource() == addBtn) {
            handleAdd();
        }
    }
    
    private void handleAdd() {
        // Gather Data
        String name = nameField.getText();
        String email = emailField.getText();
        String pass = passField.getText();
        String confPass = confPassField.getText();
        LocalDate dob = dobPicker.getValue();
        String role = roleCombo.getValue();
        
        String gender = maleRb.isSelected() ? "Male" : "Female";
        
        // Call Controller
        // Source 14: Admin adds new employees
        String result = userController.register(name, email, pass, confPass, gender, dob, role);
        
        if (result.equals("Success")) {
            alert.setAlertType(AlertType.INFORMATION);
            alert.setContentText("Employee Hired Successfully!");
            alert.showAndWait();
            refreshTable();
            clearForm();
        } else {
            alert.setAlertType(AlertType.ERROR);
            alert.setContentText(result);
            alert.show();
        }
    }
}