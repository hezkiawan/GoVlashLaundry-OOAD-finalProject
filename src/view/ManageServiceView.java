package view;

import java.util.Vector;

import controller.ServiceController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Service;

public class ManageServiceView extends BorderPane implements EventHandler<ActionEvent> {

	// --- MAIN VIEW COMPONENTS ---
    private VBox mainLayout;
    private Label titleLbl;
    private TableView<Service> table;
    private Button addBtn, updateBtn, deleteBtn;
    private HBox mainBtnBox;

    // --- FORM VIEW COMPONENTS ---
    private VBox formLayout;
    private GridPane formGrid;
    private Label formTitleLbl;
    private TextField nameField, priceField, durationField;
    private TextArea descField;
    private Button saveBtn, cancelBtn;

    // --- SHARED ---
    private Alert alert;
    private ServiceController serviceController = new ServiceController();
    private Vector<Service> services;
    
    // State Tracking
    private Service serviceToUpdate = null; // Null = Add Mode, Object = Update Mode

    public ManageServiceView() {
        initComp();
        initPos();
        refreshTable();
    }

    private void initComp() {
        // --- 1. TABLE VIEW SETUP ---
        titleLbl = new Label("Manage Laundry Services");
        titleLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        table = new TableView<>();
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setupTableColumns();

        addBtn = new Button("Add New Service");
        updateBtn = new Button("Update Selected");
        deleteBtn = new Button("Delete Selected");
        
        addBtn.setOnAction(this);
        updateBtn.setOnAction(this);
        deleteBtn.setOnAction(this);
        
        mainBtnBox = new HBox(10);
        mainBtnBox.setAlignment(Pos.CENTER);
        mainBtnBox.getChildren().addAll(addBtn, updateBtn, deleteBtn);

        // --- 2. FORM VIEW SETUP ---
        formTitleLbl = new Label("Service Details"); // Will change dynamically
        formTitleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        nameField = new TextField();
        nameField.setPromptText("Service Name");
        
        descField = new TextArea();
        descField.setPromptText("Description");
        descField.setPrefRowCount(3); 
        descField.setWrapText(true);  
        
        priceField = new TextField();
        priceField.setPromptText("Price (e.g., 15000)");
        
        durationField = new TextField();
        durationField.setPromptText("Duration (Days)");

        saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveBtn.setOnAction(this);
        
        cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelBtn.setOnAction(this);

        // --- 3. LAYOUT CONTAINERS ---
        mainLayout = new VBox(20);
        formLayout = new VBox(20);
        formGrid = new GridPane();
        
        alert = new Alert(AlertType.NONE);
    }

    private void setupTableColumns() {
        TableColumn<Service, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        
        TableColumn<Service, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        nameCol.setMinWidth(150);
        
        TableColumn<Service, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("serviceDesc"));
        descCol.setMinWidth(200);
        
        TableColumn<Service, Integer> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        
        TableColumn<Service, Integer> durCol = new TableColumn<>("Duration");
        durCol.setCellValueFactory(new PropertyValueFactory<>("serviceDuration"));
        
        table.getColumns().add(idCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(descCol);
        table.getColumns().add(priceCol);
        table.getColumns().add(durCol);
    }

    private void initPos() {
        // --- FORM LAYOUT CONSTRUCTION ---
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.CENTER);
        
        formGrid.add(new Label("Service Name:"), 0, 0);
        formGrid.add(nameField, 1, 0);
        
        formGrid.add(new Label("Description:"), 0, 1);
        formGrid.add(descField, 1, 1);
        
        formGrid.add(new Label("Price:"), 0, 2);
        formGrid.add(priceField, 1, 2);
        
        formGrid.add(new Label("Duration (Days):"), 0, 3);
        formGrid.add(durationField, 1, 3);
        
        HBox formBtns = new HBox(10);
        formBtns.setAlignment(Pos.CENTER);
        formBtns.getChildren().addAll(saveBtn, cancelBtn);
        
        formLayout.getChildren().addAll(formTitleLbl, formGrid, formBtns);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(20));

        // --- MAIN LAYOUT CONSTRUCTION ---
        // We add BOTH layouts to the VBox, but we will toggle their visibility/management
        mainLayout.getChildren().addAll(titleLbl, table, mainBtnBox, formLayout);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        this.setCenter(mainLayout);
        
        // Start in Table Mode
        showTableMode(true);
    }

    // Toggle between Table and Form
    private void showTableMode(boolean showTable) {
        // Table Components
        titleLbl.setVisible(showTable);
        titleLbl.setManaged(showTable);
        
        table.setVisible(showTable);
        table.setManaged(showTable);
        
        mainBtnBox.setVisible(showTable);
        mainBtnBox.setManaged(showTable);
        
        // Form Components
        formLayout.setVisible(!showTable);
        formLayout.setManaged(!showTable);
    }

    public void refreshTable() {
        services = serviceController.getAllServices();
        ObservableList<Service> serviceObs = FXCollections.observableArrayList(services);
        table.setItems(serviceObs);
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == addBtn) {
            openForm(null); // Add Mode
        } 
        else if (e.getSource() == updateBtn) {
            Service selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(AlertType.ERROR, "Please select a service to update.");
                return;
            }
            openForm(selected); // Update Mode
        } 
        else if (e.getSource() == deleteBtn) {
            handleDelete();
        } 
        else if (e.getSource() == saveBtn) {
            handleSave();
        } 
        else if (e.getSource() == cancelBtn) {
            showTableMode(true); // Back to table
            clearFormInputs();
        }
    }

    // Prepares the form for either Add or Update
    private void openForm(Service s) {
        this.serviceToUpdate = s; // Set state
        
        if (s == null) {
            formTitleLbl.setText("Add New Service");
            clearFormInputs();
        } else {
            formTitleLbl.setText("Update Service (ID: " + s.getServiceId() + ")");
            nameField.setText(s.getServiceName());
            descField.setText(s.getServiceDesc());
            priceField.setText(String.valueOf(s.getServicePrice()));
            durationField.setText(String.valueOf(s.getServiceDuration()));
        }
        
        showTableMode(false); // Hide table, show form
    }

    private void handleSave() {
        String idStr = (serviceToUpdate == null) ? "0" : String.valueOf(serviceToUpdate.getServiceId());
        String name = nameField.getText();
        String desc = descField.getText();
        String price = priceField.getText();
        String duration = durationField.getText();
        boolean isUpdate = (serviceToUpdate != null);

        // Call Controller
        String result = serviceController.saveService(idStr, name, desc, price, duration, isUpdate);

        if (result.equals("Success")) {
            showAlert(AlertType.INFORMATION, "Service Saved Successfully!");
            refreshTable();
            showTableMode(true); // Go back to table
            clearFormInputs();
        } else {
            showAlert(AlertType.ERROR, result);
        }
    }

    private void handleDelete() {
        Service selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(AlertType.ERROR, "Please select a service to delete.");
            return;
        }
        
        String result = serviceController.deleteService(selected.getServiceId());
        
        if(result.equals("Success")) {
            showAlert(AlertType.INFORMATION, "Service Deleted Successfully.");
            refreshTable();
        } else {
            showAlert(AlertType.ERROR, result);
        }
    }
    
    private void clearFormInputs() {
        nameField.clear();
        descField.clear();
        priceField.clear();
        durationField.clear();
    }

    private void showAlert(AlertType type, String message) {
        alert.setAlertType(type);
        alert.setContentText(message);
        alert.show();
    }
}