package view;

import java.sql.Date;
import java.util.Vector;
import java.util.stream.Collectors;

import controller.NotificationController;
import controller.TransactionController;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Transaction;

public class AdminTransactionView extends BorderPane implements EventHandler<ActionEvent> {

    // Components
    private VBox mainLayout;
    private Label titleLbl;
    private TableView<Transaction> table;
    
    // Buttons 
    private Button sendNotifBtn;
    
    // Filtering with toggle buttons
    private ToggleButton showAllBtn, showFinishedBtn;
    private ToggleGroup filterGroup;

    private Alert alert;

    // Controllers
    private TransactionController trController = new TransactionController();
    private NotificationController notifController = new NotificationController();
    
    // Data List
    private Vector<Transaction> allTransactions;

    public AdminTransactionView() {
        initComp();
        initPos();
        loadData(); 
    }

    private void initComp() {
        mainLayout = new VBox(20); 
        
        titleLbl = new Label("All Transactions Management");
        titleLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Initialize table
        table = new TableView<>();
        setupTableColumns();

        // Initialize button to send notification
        sendNotifBtn = new Button("Send Completion Notification");
        sendNotifBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;");
        sendNotifBtn.setOnAction(this);

        // Initialize gilter Buttons 
        showAllBtn = new ToggleButton("Show All");
        showFinishedBtn = new ToggleButton("Show Finished Only");
        
        // Group toggle button so only one is active at a time
        filterGroup = new ToggleGroup();
        showAllBtn.setToggleGroup(filterGroup);
        showFinishedBtn.setToggleGroup(filterGroup);
        showAllBtn.setSelected(true); // Default to Show All

        // Add event listeners to toggles
        showAllBtn.setOnAction(this);
        showFinishedBtn.setOnAction(this);

        alert = new Alert(AlertType.NONE);
    }

    private void setupTableColumns() {
        TableColumn<Transaction, Integer> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        idCol.setMinWidth(80);
        
        TableColumn<Transaction, String> custCol = new TableColumn<>("Customer Name");
        custCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custCol.setMinWidth(120);
        
        TableColumn<Transaction, String> servCol = new TableColumn<>("Service Name");
        servCol.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        servCol.setMinWidth(120);
        
        TableColumn<Transaction, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        dateCol.setMinWidth(100);
        
        TableColumn<Transaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("transactionStatus"));
        statusCol.setMinWidth(100);
        
        TableColumn<Transaction, Integer> weightCol = new TableColumn<>("Weight (Kg)");
        weightCol.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));
        
        TableColumn<Transaction, Integer> priceCol = new TableColumn<>("Total Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
                
        table.getColumns().add(idCol);
        table.getColumns().add(custCol);
        table.getColumns().add(servCol);
        table.getColumns().add(dateCol);
        table.getColumns().add(statusCol);
        table.getColumns().add(weightCol);
        table.getColumns().add(priceCol);
    }

    private void initPos() {
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);
        
        // Create a horizontal box for the filter buttons
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.getChildren().addAll(new Label("Filter: "), showAllBtn, showFinishedBtn);

        // Add everything to main layout
        mainLayout.getChildren().addAll(titleLbl, filterBox, table, sendNotifBtn);
        
        this.setCenter(mainLayout);
    }

    private void loadData() {
        // View all transactions
        allTransactions = trController.getAllTransactions();
        
        // Apply filter
        applyFilter();
    }
    
    private void applyFilter() {
        // Check which toggle is selected
        if (showFinishedBtn.isSelected()) {
            // Filter list to only show "Finished"
            Vector<Transaction> filteredList = allTransactions.stream()
                .filter(t -> t.getTransactionStatus().equalsIgnoreCase("Finished"))
                .collect(Collectors.toCollection(Vector::new));
            
            ObservableList<Transaction> trObs = FXCollections.observableArrayList(filteredList);
            table.setItems(trObs);
            
        } else {
            // Show All
            ObservableList<Transaction> trObs = FXCollections.observableArrayList(allTransactions);
            table.setItems(trObs);
        }
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == sendNotifBtn) {
            handleSendNotification();
        } else if (e.getSource() == showAllBtn || e.getSource() == showFinishedBtn) {
            applyFilter();
        }
    }

    private void handleSendNotification() {
        Transaction selectedTr = table.getSelectionModel().getSelectedItem();

        // Validation 1 admin must select a row
        if (selectedTr == null) {
            showAlert(AlertType.ERROR, "Please select a transaction first.");
            return;
        }

        // Validation 2 only for finished transaction
        if (!selectedTr.getTransactionStatus().equalsIgnoreCase("Finished")) {
            showAlert(AlertType.WARNING, "You can only send notifications for 'Finished' orders.");
            return;
        }

        // Call notification controller
        int customerId = selectedTr.getCustomerId();
        
        // Send notification
        String result = notifController.sendNotification(customerId);

        if (result.equals("Success")) {
            showAlert(AlertType.INFORMATION, "Notification sent successfully to Customer ID " + customerId);
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