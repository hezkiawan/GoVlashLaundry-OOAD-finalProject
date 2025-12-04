package controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Vector;

import database.TransactionDAO;
import model.Transaction;

public class TransactionController {
	private TransactionDAO trDAO = new TransactionDAO();

    // VIEW METHODS
    public Vector<Transaction> getAllTransactions() { return trDAO.getAllTransactions(); }
    public Vector<Transaction> getCustomerTransactions(int customerId) { return trDAO.getTransactionsByCustomer(customerId); }
    public Vector<Transaction> getPendingTransactions() { return trDAO.getPendingTransactions(); }
    public Vector<Transaction> getStaffTasks(int staffId) { return trDAO.getTransactionsByStaff(staffId); }

    // CREATE TRANSACTION (Customer)
    public String createTransaction(int serviceId, int customerId, String weightStr, String notes) {
        
        // 1. Validate Service ID (Source 45: Cannot be empty)
        if (serviceId == 0) return "Please select a service.";

        // 2. Validate Weight (Source 45: Must be between 2 and 50kg)
        int weight = 0;
        try {
            weight = Integer.parseInt(weightStr);
        } catch (NumberFormatException e) {
            return "Weight must be numeric.";
        }
        if (weight < 2 || weight > 50) return "Total Weight must be between 2 and 50 kg.";

        // 3. Validate Notes (Source 48, 50: <= 250 characters)
        // (Note: Null check handled by textfield usually returning empty string, but safety check added)
        if (notes != null && notes.length() > 250) {
            return "Notes must be <= 250 characters.";
        }

        // Create Object
        // Status is 'Pending' by default in DB, Date is Now
        Transaction tr = new Transaction(0, serviceId, customerId, 0, 0, Date.valueOf(LocalDate.now()), "Pending", weight, notes);
        trDAO.createTransaction(tr);

        return "Success";
    }

    // ASSIGN STAFF (Receptionist)
    // Receptionist ID and Laundry Staff ID set here
    public String assignStaff(int transactionId, int receptionistId, int staffId) {
        if (staffId == 0) return "Please select a Laundry Staff.";
        
        trDAO.assignStaff(transactionId, receptionistId, staffId);
        return "Success";
    }

    // COMPLETE ORDER (Laundry Staff)
    // Changes status to "Finished"
    public void completeOrder(int transactionId) {
        trDAO.completeTransaction(transactionId);
    }
}
