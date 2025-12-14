package view;

import java.util.Vector;

import controller.ServiceController;
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
import main.UserSession;
import model.Service;

public class BuyServiceView extends BorderPane implements EventHandler<ActionEvent> {
	// Components for Main View
    private VBox mainLayout;
    private Label titleLbl, noServiceLbl;
    private TableView<Service> table;
    private Button orderServiceBtn; 
    
    // Components for Order Form
    private VBox formLayout;
    private GridPane formGrid;
    private Label formTitleLbl;
    
    // Service details labels in form
    private Label detailNameLbl, detailDescLbl, detailPriceLbl;
    private Label valNameLbl, valDescLbl, valPriceLbl; 
    
    // Input fields
    private Label weightLbl, noteLbl;
    private TextField weightField;
    private TextArea noteArea;
    private Button submitBtn, cancelBtn;
    
    private Alert alert;

    // Controllers
    private ServiceController serviceController = new ServiceController();
    private TransactionController transactionController = new TransactionController();
    
    // Data
    private Vector<Service> services;
    private Service selectedService = null;

    public BuyServiceView() {
        initComp();
        initPos();
        refreshTable(); 
    }

    private void initComp() {
        mainLayout = new VBox(20);
        
        // MAIN VIEW COMPONENTS
        titleLbl = new Label("Buy Laundry Service");
        titleLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        noServiceLbl = new Label("No services available.");
        noServiceLbl.setStyle("-fx-text-fill: red;");
        noServiceLbl.setVisible(false);

        table = new TableView<>();
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setupTableColumns();

        orderServiceBtn = new Button("Order Selected Service");
        orderServiceBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        orderServiceBtn.setOnAction(this);

        // FORM VIEW COMPONENTS
        formLayout = new VBox(20);
        formGrid = new GridPane();
        
        formTitleLbl = new Label("Order Details");
        formTitleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Labels for displaying Service Info
        detailNameLbl = new Label("Service Name:");
        valNameLbl = new Label("-");
        valNameLbl.setStyle("-fx-font-weight: bold;");
        
        detailDescLbl = new Label("Description:");
        valDescLbl = new Label("-");
        
        detailPriceLbl = new Label("Price / Kg:");
        valPriceLbl = new Label("-");

        // Inputs
        weightLbl = new Label("Total Weight (Kg):");
        weightField = new TextField();
        weightField.setPromptText("Min 2kg, Max 50kg");
        
        noteLbl = new Label("Notes:");
        noteArea = new TextArea();
        noteArea.setPromptText("Optional notes (max 250 chars)");
        noteArea.setPrefRowCount(3);
        noteArea.setWrapText(true);

        submitBtn = new Button("Submit Order");
        submitBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        submitBtn.setOnAction(this);
        
        cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelBtn.setOnAction(this);
        
        alert = new Alert(AlertType.NONE);
    }

    private void setupTableColumns() {
        TableColumn<Service, String> nameCol = new TableColumn<>("Service Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        nameCol.setMinWidth(150);
        
        TableColumn<Service, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("serviceDesc"));
        descCol.setMinWidth(200);
        
        TableColumn<Service, Integer> priceCol = new TableColumn<>("Price / Kg");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        
        TableColumn<Service, Integer> durCol = new TableColumn<>("Est. Duration (Days)");
        durCol.setCellValueFactory(new PropertyValueFactory<>("serviceDuration"));
        
        table.getColumns().add(nameCol);
        table.getColumns().add(descCol);
        table.getColumns().add(priceCol);
        table.getColumns().add(durCol);
    }

    private void initPos() {
        // FORM GRID SETUP
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.CENTER);
        
        // Service details
        formGrid.add(detailNameLbl, 0, 0);
        formGrid.add(valNameLbl, 1, 0);
        
        formGrid.add(detailDescLbl, 0, 1);
        formGrid.add(valDescLbl, 1, 1);
        
        formGrid.add(detailPriceLbl, 0, 2);
        formGrid.add(valPriceLbl, 1, 2);
        
        // Input fields
        formGrid.add(weightLbl, 0, 3);
        formGrid.add(weightField, 1, 3);
        
        formGrid.add(noteLbl, 0, 4);
        formGrid.add(noteArea, 1, 4);
        
        // Buttons
        HBox formBtns = new HBox(10);
        formBtns.setAlignment(Pos.CENTER);
        formBtns.getChildren().addAll(submitBtn, cancelBtn);
        
        formLayout.getChildren().addAll(formTitleLbl, formGrid, formBtns);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(20));
        
        // MAIN LAYOUT SETUP
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);
        
        // Add both views to main layout
        mainLayout.getChildren().addAll(titleLbl, noServiceLbl, table, orderServiceBtn, formLayout);

        this.setCenter(mainLayout);
        
        // Show table but hide form
        showTableMode(true); 
    }

    private void refreshTable() {
        services = serviceController.getAllServices();
        
        if (services.isEmpty()) {
            noServiceLbl.setVisible(true);
            table.setVisible(false);
            orderServiceBtn.setVisible(false);
        } else {
            noServiceLbl.setVisible(false);
            table.setVisible(true);
            orderServiceBtn.setVisible(true);
            
            ObservableList<Service> serviceObs = FXCollections.observableArrayList(services);
            table.setItems(serviceObs);
        }
    }

    // Helper to toggle between Table View and Form View
    private void showTableMode(boolean showTable) {
        // Table components
        titleLbl.setVisible(showTable);
        table.setVisible(showTable);
        orderServiceBtn.setVisible(showTable);
        
        // Form components
        formLayout.setVisible(!showTable);
        formLayout.setManaged(!showTable); 
        
        // Table managed property
        table.setManaged(showTable);
        orderServiceBtn.setManaged(showTable);
        titleLbl.setManaged(showTable);
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == orderServiceBtn) {
            handleOrderClick();
        } else if (e.getSource() == submitBtn) {
            handleSubmit();
        } else if (e.getSource() == cancelBtn) {
            showTableMode(true); // Go back
        }
    }

    private void handleOrderClick() {
        Service selection = table.getSelectionModel().getSelectedItem();
        
        if (selection == null) {
            alert.setAlertType(AlertType.ERROR);
            alert.setContentText("Please select a service first.");
            alert.show();
            return;
        }
        
        selectedService = selection;
        
        // Populate form labels
        valNameLbl.setText(selectedService.getServiceName());
        valDescLbl.setText(selectedService.getServiceDesc());
        valPriceLbl.setText("Rp. " + selectedService.getServicePrice());
        
        // Clear inputs
        weightField.clear();
        noteArea.clear();
        
        // Switch view
        showTableMode(false);
    }

    private void handleSubmit() {
        int serviceId = (selectedService != null) ? selectedService.getServiceId() : 0;
        int customerId = UserSession.getInstance().getId();
        
        String weightStr = weightField.getText();
        String notes = noteArea.getText();
        
        String result = transactionController.createTransaction(serviceId, customerId, weightStr, notes);
        
        if (result.equals("Success")) {
            alert.setAlertType(AlertType.INFORMATION);
            alert.setContentText("Order Placed Successfully!");
            alert.showAndWait();
            
            // Return to table view
            table.getSelectionModel().clearSelection();
            selectedService = null;
            showTableMode(true);
            
        } else {
            alert.setAlertType(AlertType.ERROR);
            alert.setContentText(result);
            alert.show();
        }
    }
}