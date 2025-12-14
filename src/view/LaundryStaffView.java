package view;

import java.sql.Date;
import java.util.Vector;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.UserSession;
import model.Transaction;

public class LaundryStaffView extends BorderPane implements EventHandler<ActionEvent> {

    // Components
    private VBox mainLayout;
    private Label titleLbl, noDataLbl;
    private TableView<Transaction> table;
    private Button completeBtn;
    private Alert alert;

    // Controller
    private TransactionController trController = new TransactionController();
    private Vector<Transaction> myTasks;

    public LaundryStaffView() {
        initComp();
        initPos();
        loadData();
    }

    private void initComp() {
        mainLayout = new VBox(20);
        
        titleLbl = new Label("My Assigned Tasks");
        titleLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        noDataLbl = new Label("No pending tasks assigned to you.");
        noDataLbl.setVisible(false);

        // Table Setup
        table = new TableView<>();
        setupTableColumns();
        
        // Button
        completeBtn = new Button("Mark as Finished");
        completeBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        completeBtn.setOnAction(this);
        
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
        
        mainLayout.getChildren().addAll(titleLbl, table, noDataLbl, completeBtn);
        
        this.setCenter(mainLayout);
    }

    private void loadData() {
        // 1. Get current staff id
        int staffId = UserSession.getInstance().getId();
        
        // 2. Fetch assigned pending tasks
        myTasks = trController.getStaffTasks(staffId);
        
        if (myTasks.isEmpty()) {
            table.setVisible(false);
            completeBtn.setVisible(false); // Hide button if no tasks
            noDataLbl.setVisible(true);
        } else {
            table.setVisible(true);
            completeBtn.setVisible(true);
            noDataLbl.setVisible(false);
            
            ObservableList<Transaction> taskObs = FXCollections.observableArrayList(myTasks);
            table.setItems(taskObs);
        }
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == completeBtn) {
            handleComplete();
        }
    }

    private void handleComplete() {
        Transaction selectedTr = table.getSelectionModel().getSelectedItem();
        
        if (selectedTr == null) {
            alert.setAlertType(AlertType.ERROR);
            alert.setContentText("Please select a task to complete.");
            alert.show();
            return;
        }
        
        // Mark as finished
        trController.completeOrder(selectedTr.getTransactionId());
        
        // Success Message
        alert.setAlertType(AlertType.INFORMATION);
        alert.setContentText("Order #" + selectedTr.getTransactionId() + " marked as Finished!");
        alert.showAndWait();
        
        // Refresh to remove the finished task from the list
        loadData();
    }
}