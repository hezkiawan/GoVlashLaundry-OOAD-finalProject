package model;

import java.sql.Date;

public class Transaction {
	private int transactionId;
    private int serviceId;
    private int customerId;
    private int receptionistId; // Can be 0 if null
    private int laundryStaffId; // Can be 0 if null
    private Date transactionDate;
    private String transactionStatus;
    private int totalWeight;
    private String transactionNotes;
    
    private String customerName;
    private String serviceName;
    private String laundryStaffName;
    private int totalPrice;

    public Transaction(int transactionId, int serviceId, int customerId, int receptionistId, int laundryStaffId,
            Date transactionDate, String transactionStatus, int totalWeight, String transactionNotes,
            String customerName, String serviceName, String laundryStaffName, int totalPrice) {
		this.transactionId = transactionId;
		this.serviceId = serviceId;
		this.customerId = customerId;
		this.receptionistId = receptionistId;
		this.laundryStaffId = laundryStaffId;
		this.transactionDate = transactionDate;
		this.transactionStatus = transactionStatus;
		this.totalWeight = totalWeight;
		this.transactionNotes = transactionNotes;
		
		this.customerName = customerName;
		this.serviceName = serviceName;
		this.laundryStaffName = laundryStaffName; 
		this.totalPrice = totalPrice;
    }
    
    public Transaction(int transactionId, int serviceId, int customerId, int receptionistId, int laundryStaffId,
            Date transactionDate, String transactionStatus, int totalWeight, String transactionNotes) {
        this.transactionId = transactionId;
        this.serviceId = serviceId;
        this.customerId = customerId;
        this.receptionistId = receptionistId;
        this.laundryStaffId = laundryStaffId;
        this.transactionDate = transactionDate;
        this.transactionStatus = transactionStatus;
        this.totalWeight = totalWeight;
        this.transactionNotes = transactionNotes;
    }

 // Getters
    public int getTransactionId() { return transactionId; }
    public int getServiceId() { return serviceId; }
    public int getCustomerId() { return customerId; }
    public int getReceptionistId() { return receptionistId; }
    public int getLaundryStaffId() { return laundryStaffId; }
    public Date getTransactionDate() { return transactionDate; }
    public String getTransactionStatus() { return transactionStatus; }
    public int getTotalWeight() { return totalWeight; }
    public String getTransactionNotes() { return transactionNotes; }
    
    // Display Getters
    public String getCustomerName() { return customerName; }
    public String getServiceName() { return serviceName; }
    public String getLaundryStaffName() { return laundryStaffName; } 
    public int getTotalPrice() { return totalPrice; }
    
}
