package view;

import java.sql.Date;
import java.util.Vector;
import java.util.stream.Collectors;

import controller.TransactionController;
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
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.UserSession;
import model.Employee;
import model.Transaction;

public class ReceptionistView extends BorderPane implements EventHandler<ActionEvent> {

	// --- MAIN TABLE VIEW COMPONENTS ---
    private VBox mainLayout;
    private Label titleLbl;
    private TableView<Transaction> table;
    private Button openAssignBtn;
    
    // Filter Components
    private ToggleButton showUnassignedBtn, showAllBtn; 
    private ToggleGroup filterGroup;

    // Form components
    private VBox formLayout;
    private GridPane formGrid;
    private Label formTitleLbl;
    
    // Form detailes lables
    private Label detIdLbl, detCustLbl, detServLbl, detDateLbl, detStatusLbl, detWeightLbl, detPriceLbl;
    private Label valIdLbl, valCustLbl, valServLbl, valDateLbl, valStatusLbl, valWeightLbl, valPriceLbl;
    
    // Form input
    private Label assignToLbl;
    private ComboBox<Employee> staffCombo;
    private Button confirmBtn, cancelBtn;

    // Controller
    private TransactionController trController = new TransactionController();
    private UserController userController = new UserController();
    
    private Vector<Transaction> allTransactions;
    private Transaction selectedTr;
    private Alert alert;

    public ReceptionistView() {
        initComp();
        initPos();
        loadData();
    }

    private void initComp() {
        // Table components
        titleLbl = new Label("Transaction Management");
        titleLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Filter toggles
        showUnassignedBtn = new ToggleButton("Show Unassigned");
        showAllBtn = new ToggleButton("Show All Transactions");
        filterGroup = new ToggleGroup();
        showUnassignedBtn.setToggleGroup(filterGroup);
        showAllBtn.setToggleGroup(filterGroup);
        showUnassignedBtn.setSelected(true); // Default to Unassigned
        
        showUnassignedBtn.setOnAction(this);
        showAllBtn.setOnAction(this);

        table = new TableView<>();
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setupTableColumns();
        
        openAssignBtn = new Button("Assign Selected Order");
        openAssignBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        openAssignBtn.setOnAction(this);

        // Form components
        formTitleLbl = new Label("Assign Order to Staff");
        formTitleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Labels for details
        detIdLbl = new Label("Order ID:");
        detCustLbl = new Label("Customer Name:");
        detServLbl = new Label("Service Name:");
        detDateLbl = new Label("Date:");
        detStatusLbl = new Label("Status:");
        detWeightLbl = new Label("Weight:");
        detPriceLbl = new Label("Total Price:");
        
        // Labels for values (Bold)
        valIdLbl = new Label("-"); valIdLbl.setStyle("-fx-font-weight: bold;");
        valCustLbl = new Label("-"); valCustLbl.setStyle("-fx-font-weight: bold;");
        valServLbl = new Label("-"); valServLbl.setStyle("-fx-font-weight: bold;");
        valDateLbl = new Label("-"); valDateLbl.setStyle("-fx-font-weight: bold;");
        valStatusLbl = new Label("-"); valStatusLbl.setStyle("-fx-font-weight: bold;");
        valWeightLbl = new Label("-"); valWeightLbl.setStyle("-fx-font-weight: bold;");
        valPriceLbl = new Label("-"); valPriceLbl.setStyle("-fx-font-weight: bold;");
        
        assignToLbl = new Label("Assign to Laundry Staff:");
        
        staffCombo = new ComboBox<>();
        staffCombo.setPromptText("Select Staff...");
        
        confirmBtn = new Button("Confirm Assignment");
        confirmBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        confirmBtn.setOnAction(this);
        
        cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelBtn.setOnAction(this);

        alert = new Alert(AlertType.NONE);
        
        // Containers
        mainLayout = new VBox(20);
        formLayout = new VBox(20);
        formGrid = new GridPane();
    }

    private void setupTableColumns() {
        TableColumn<Transaction, Integer> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        idCol.setMinWidth(50);
        
        TableColumn<Transaction, String> custCol = new TableColumn<>("Customer Name");
        custCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custCol.setMinWidth(120);
        
        TableColumn<Transaction, String> servCol = new TableColumn<>("Service Name");
        servCol.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        servCol.setMinWidth(120);
        
        TableColumn<Transaction, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        
        // Staff name column
        TableColumn<Transaction, String> staffCol = new TableColumn<>("Staff Name");
        staffCol.setCellValueFactory(new PropertyValueFactory<>("laundryStaffName"));
        staffCol.setMinWidth(120);
        
        TableColumn<Transaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("transactionStatus"));
        
        TableColumn<Transaction, Integer> weightCol = new TableColumn<>("Weight (Kg)");
        weightCol.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));
        
        TableColumn<Transaction, Integer> priceCol = new TableColumn<>("Total Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        
        table.getColumns().add(idCol);
        table.getColumns().add(custCol);
        table.getColumns().add(servCol);
        table.getColumns().add(dateCol);
        table.getColumns().add(staffCol);
        table.getColumns().add(statusCol);
        table.getColumns().add(weightCol);
        table.getColumns().add(priceCol);
    }

    private void initPos() {
        // Form layout 
        formGrid.setHgap(15);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);
        
        formGrid.add(detIdLbl, 0, 0); formGrid.add(valIdLbl, 1, 0);
        formGrid.add(detCustLbl, 0, 1); formGrid.add(valCustLbl, 1, 1);
        formGrid.add(detServLbl, 0, 2); formGrid.add(valServLbl, 1, 2);
        formGrid.add(detDateLbl, 0, 3); formGrid.add(valDateLbl, 1, 3);
        formGrid.add(detStatusLbl, 0, 4); formGrid.add(valStatusLbl, 1, 4);
        formGrid.add(detWeightLbl, 0, 5); formGrid.add(valWeightLbl, 1, 5);
        formGrid.add(detPriceLbl, 0, 6); formGrid.add(valPriceLbl, 1, 6);
        
        formGrid.add(new Label(""), 0, 7); 
        
        formGrid.add(assignToLbl, 0, 8);
        formGrid.add(staffCombo, 1, 8);
        
        HBox formBtns = new HBox(10);
        formBtns.setAlignment(Pos.CENTER);
        formBtns.getChildren().addAll(confirmBtn, cancelBtn);
        
        formLayout.getChildren().addAll(formTitleLbl, formGrid, formBtns);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(20));

        // Main layout
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER);
        filterBox.getChildren().addAll(new Label("Filter:"), showUnassignedBtn, showAllBtn);
        
        // Add layouts to VBox
        mainLayout.getChildren().addAll(titleLbl, filterBox, table, openAssignBtn, formLayout);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        
        this.setCenter(mainLayout);
        
        // Start in table mode
        showTableMode(true);
    }

    private void showTableMode(boolean showTable) {
        // Table components
        titleLbl.setVisible(showTable);
        showUnassignedBtn.getParent().setVisible(showTable); // Hide Filter Box
        table.setVisible(showTable);
        openAssignBtn.setVisible(showTable);
        
        // Manage state (collapse space)
        titleLbl.setManaged(showTable);
        showUnassignedBtn.getParent().setManaged(showTable);
        table.setManaged(showTable);
        openAssignBtn.setManaged(showTable);
        
        // Form components
        formLayout.setVisible(!showTable);
        formLayout.setManaged(!showTable);
    }

    private void loadData() {
        // 1. Fetch all transactions
        allTransactions = trController.getAllTransactions(); 
        applyFilter();

        // 2. Load Laundry Staff into ComboBox
        staffCombo.getItems().clear();
        Vector<Employee> allEmployees = userController.getAllEmployees();
        
        for (Employee emp : allEmployees) {
            if (emp.getRole().equals("Laundry Staff")) {
                staffCombo.getItems().add(emp);
            }
        }
    }
    
    private void applyFilter() {
        if (allTransactions == null) return;

        ObservableList<Transaction> trObs;
   
        if (showUnassignedBtn.isSelected()) {
            // Unassigned" ==  laundryStaff is 0
            Vector<Transaction> unassigned = allTransactions.stream()
                .filter(t -> t.getLaundryStaffId() == 0) 
                .collect(Collectors.toCollection(Vector::new));
            trObs = FXCollections.observableArrayList(unassigned);
        } else {
            // Show All
            trObs = FXCollections.observableArrayList(allTransactions);
        }
        
        table.setItems(trObs);
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == showUnassignedBtn || e.getSource() == showAllBtn) {
            applyFilter();
        } 
        else if (e.getSource() == openAssignBtn) {
            handleAssignClick();
        } 
        else if (e.getSource() == confirmBtn) {
            handleConfirmAssign();
        } 
        else if (e.getSource() == cancelBtn) {
            showTableMode(true);
            staffCombo.getSelectionModel().clearSelection();
        }
    }

    private void handleAssignClick() {
        selectedTr = table.getSelectionModel().getSelectedItem();
        
        if (selectedTr == null) {
            showAlert(AlertType.ERROR, "Please select a transaction to assign.");
            return;
        }
        
        // Validation to ensure no re-assigning finished tasks
        if (selectedTr.getTransactionStatus().equalsIgnoreCase("Finished")) {
             showAlert(AlertType.WARNING, "Cannot assign a finished transaction.");
             return;
        }
        
        // Fill form labels
        valIdLbl.setText(String.valueOf(selectedTr.getTransactionId()));
        valCustLbl.setText(selectedTr.getCustomerName());
        valServLbl.setText(selectedTr.getServiceName());
        valDateLbl.setText(selectedTr.getTransactionDate().toString());
        valStatusLbl.setText(selectedTr.getTransactionStatus());
        valWeightLbl.setText(selectedTr.getTotalWeight() + " Kg");
        valPriceLbl.setText("Rp. " + selectedTr.getTotalPrice());
        
        staffCombo.getSelectionModel().clearSelection();
        
        // Switch view
        showTableMode(false);
    }

    private void handleConfirmAssign() {
        Employee selectedStaff = staffCombo.getValue();
        
        if (selectedStaff == null) {
            showAlert(AlertType.ERROR, "Please select a Laundry Staff.");
            return;
        }
        
        // ID for update
        int trId = selectedTr.getTransactionId();
        int staffId = selectedStaff.getId();
        int receptionistId = UserSession.getInstance().getId();
        
        String result = trController.assignStaff(trId, receptionistId, staffId);
        
        if (result.equals("Success")) {
            showAlert(AlertType.INFORMATION, "Task Assigned Successfully!");
            
            // Reload data and go back to table
            loadData(); 
            showTableMode(true);
        } else {
            showAlert(AlertType.ERROR, result);
        }
    }
    
    private void showAlert(AlertType type, String msg) {
        alert.setAlertType(type);
        alert.setContentText(msg);
        alert.show();
    }
}