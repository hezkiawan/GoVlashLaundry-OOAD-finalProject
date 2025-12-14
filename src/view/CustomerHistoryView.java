package view;

import java.sql.Date;
import java.util.Vector;

import controller.TransactionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.UserSession;
import model.Transaction;

public class CustomerHistoryView extends BorderPane {

    // Components
    private VBox mainLayout;
    private Label titleLbl, noDataLbl;
    private TableView<Transaction> table;

    // Controller
    private TransactionController trController = new TransactionController();
    private Vector<Transaction> transactions;

    public CustomerHistoryView() {
        initComp();
        initPos();
        refreshTable();
    }

    private void initComp() {
        mainLayout = new VBox(20);
        
        titleLbl = new Label("My Transaction History");
        titleLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        noDataLbl = new Label("No transactions found.");
        noDataLbl.setVisible(false); 

        // Table setup
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
        
        setupTableColumns();
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
        mainLayout.setAlignment(Pos.TOP_CENTER);
        
        mainLayout.getChildren().addAll(titleLbl, table, noDataLbl);
        
        this.setCenter(mainLayout);
    }

    private void refreshTable() {
        // 1. Get current user ID
        int userId = UserSession.getInstance().getId();
        
        // 2. Fetch data
        transactions = trController.getCustomerTransactions(userId);
        
        // 3. Fill table
        if (transactions.isEmpty()) {
            table.setVisible(false);
            noDataLbl.setVisible(true);
        } else {
            table.setVisible(true);
            noDataLbl.setVisible(false);
            ObservableList<Transaction> trObs = FXCollections.observableArrayList(transactions);
            table.setItems(trObs);
        }
    }
}