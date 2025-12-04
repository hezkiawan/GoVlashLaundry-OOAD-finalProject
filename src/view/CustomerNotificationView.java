package view;

import java.sql.Timestamp;
import java.util.Vector;

import controller.NotificationController;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.UserSession;
import model.Notification;

public class CustomerNotificationView extends BorderPane implements EventHandler<ActionEvent> {

    // Components
    private VBox mainLayout;
    private Label titleLbl, detailLbl;
    private TableView<Notification> table;
    private TextArea messageDetailArea;
    private Button deleteBtn;
    private Alert alert;

    // Controller
    private NotificationController noteController = new NotificationController();
    private Vector<Notification> notifications;

    public CustomerNotificationView() {
        initComp();
        initPos();
        loadData();
    }

    private void initComp() {
        mainLayout = new VBox(20);
        
        titleLbl = new Label("My Notifications");
        titleLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Table Setup
        table = new TableView<>();
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setupTableColumns();

        // LISTENER: View Detail & Mark as Read logic 
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                handleSelection(newVal);
            }
        });

        // Detail Area
        detailLbl = new Label("Message Detail:");
        messageDetailArea = new TextArea();
        messageDetailArea.setEditable(false);
        messageDetailArea.setWrapText(true);
        messageDetailArea.setPrefHeight(100);
        messageDetailArea.setPromptText("Select a notification to view details.");

        // Delete Button 
        deleteBtn = new Button("Delete Notification");
        deleteBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        deleteBtn.setOnAction(this);
        
        alert = new Alert(AlertType.NONE);
    }

    private void setupTableColumns() {
        // ID Column
        TableColumn<Notification, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setMinWidth(50);
        
        // Time Column
        TableColumn<Notification, Timestamp> timeCol = new TableColumn<>("Date Received");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        timeCol.setMinWidth(150);
        
        // Message Preview Column
        TableColumn<Notification, String> msgCol = new TableColumn<>("Message Preview");
        msgCol.setCellValueFactory(new PropertyValueFactory<>("message"));
        msgCol.setMinWidth(300);
        
        // Read Status Column (Source 26)
        TableColumn<Notification, Boolean> readCol = new TableColumn<>("Read");
        readCol.setCellValueFactory(new PropertyValueFactory<>("isRead"));
        
//        table.getColumns().addAll(idCol, timeCol, msgCol, readCol);
        
        table.getColumns().add(idCol);
        table.getColumns().add(timeCol);
        table.getColumns().add(msgCol);
        table.getColumns().add(readCol);
    }

    private void initPos() {
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);
        
        // Layout: Title -> Table -> Label -> TextArea -> Delete Button
        mainLayout.getChildren().addAll(titleLbl, table, detailLbl, messageDetailArea, deleteBtn);
        
        this.setCenter(mainLayout);
    }

    private void loadData() {
        int userId = UserSession.getInstance().getId();
        notifications = noteController.getMyNotifications(userId);
        
        ObservableList<Notification> noteObs = FXCollections.observableArrayList(notifications);
        table.setItems(noteObs);
    }

    // Handle viewing details and marking as read
    private void handleSelection(Notification n) {
        // 1. Show Detail
        messageDetailArea.setText(n.getMessage());
        
        // 2. Mark as Read if not already read 
        if (!n.isRead()) {
            noteController.markAsRead(n.getId());
            // We reload data to show the updated "true" status in the table
            // However, to keep the selection active, we just update the list if possible, 
            // or simple reload (which might deselect). Simple reload is safer for data consistency.
            loadData();
        }
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == deleteBtn) {
            handleDelete();
        }
    }

    private void handleDelete() {
        Notification selected = table.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            alert.setAlertType(AlertType.ERROR);
            alert.setContentText("Please select a notification to delete.");
            alert.show();
            return;
        }
        
        // Call Controller to Delete 
        noteController.deleteNotification(selected.getId());
        
        alert.setAlertType(AlertType.INFORMATION);
        alert.setContentText("Notification deleted.");
        alert.showAndWait();
        
        // Clear UI
        messageDetailArea.clear();
        loadData();
    }
}