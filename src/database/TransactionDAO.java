package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.Transaction;

public class TransactionDAO {
	private Connect connect = Connect.getInstance();

    // Helper to map DB row to Object so its easy to add to vector
    private Transaction mapResultSet(ResultSet rs) throws SQLException {
    	
    	int weight = rs.getInt("TotalWeight");
        int pricePerKg = rs.getInt("ServicePrice"); 
        int total = weight * pricePerKg;
        
        return new Transaction(
            rs.getInt("TransactionId"),
            rs.getInt("ServiceId"),
            rs.getInt("CustomerId"),
            rs.getInt("ReceptionistId"),
            rs.getInt("LaundryStaffId"),
            rs.getDate("TransactionDate"),
            rs.getString("TransactionStatus"),
            weight,
            rs.getString("TransactionNotes"),
            rs.getString("CustomerName"),
            rs.getString("ServiceName"),
            rs.getString("StaffName"),
            total
        );
    }
    
    
    // base query
    private final String BASE_QUERY = "SELECT t.*, " +
            "u.UserName AS CustomerName, " +
            "s.ServiceName, " +
            "s.ServicePrice, " +
            "st.UserName AS StaffName " + 
            "FROM tr_transaction t " +
            "JOIN ms_user u ON t.CustomerId = u.UserId " +
            "JOIN ms_service s ON t.ServiceId = s.ServiceId " +
            "LEFT JOIN ms_user st ON t.LaundryStaffId = st.UserId "; 
    // VIEW ALL (For Admin)
    public Vector<Transaction> getAllTransactions() {
        Vector<Transaction> trList = new Vector<>();
        String query = BASE_QUERY + "ORDER BY TransactionDate DESC, TransactionId DESC";
        ResultSet rs = connect.execQuery(query);
        try {
            while(rs.next()) trList.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return trList;
    }

    // CUSTOMER HISTORY
    public Vector<Transaction> getTransactionsByCustomer(int customerId) {
        Vector<Transaction> trList = new Vector<>();
        String query = BASE_QUERY + "WHERE t.CustomerId = ? ORDER BY t.TransactionDate DESC, t.TransactionId DESC";
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            if (ps == null) return trList;
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) trList.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return trList;
    }

    // PENDING (For Receptionist)
    public Vector<Transaction> getPendingTransactions() {
        Vector<Transaction> trList = new Vector<>();
        String query = BASE_QUERY + "WHERE t.TransactionStatus = 'Pending' ORDER BY t.TransactionDate DESC, t.TransactionId DESC";
        ResultSet rs = connect.execQuery(query);
        try {
            while(rs.next()) trList.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return trList;
    }

    // STAFF ASSIGNED TASKS (For Laundry Staff) 
    public Vector<Transaction> getTransactionsByStaff(int staffId) {
        Vector<Transaction> trList = new Vector<>();
        String query = BASE_QUERY + "WHERE t.LaundryStaffId = ? AND t.TransactionStatus = 'Pending' ORDER BY TransactionDate DESC, TransactionId DESC";
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            if (ps == null) return trList;
            ps.setInt(1, staffId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) trList.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return trList;
    }

    // CREATE TRANSACTION (Customer)
    public void createTransaction(Transaction tr) {
        String query = "INSERT INTO tr_transaction (ServiceId, CustomerId, TransactionDate, TransactionStatus, TotalWeight, TransactionNotes) VALUES (?, ?, ?, 'Pending', ?, ?)";
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            if (ps == null) return;
            ps.setInt(1, tr.getServiceId());
            ps.setInt(2, tr.getCustomerId());
            ps.setDate(3, tr.getTransactionDate()); 
            ps.setInt(4, tr.getTotalWeight());
            ps.setString(5, tr.getTransactionNotes());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ASSIGN STAFF (Receptionist)
    public void assignStaff(int transactionId, int receptionistId, int staffId) {
        String query = "UPDATE tr_transaction SET ReceptionistId = ?, LaundryStaffId = ? WHERE TransactionId = ?";
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            if (ps == null) return;
            ps.setInt(1, receptionistId);
            ps.setInt(2, staffId);
            ps.setInt(3, transactionId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // COMPLETE ORDER (Laundry Staff) 
    public void completeTransaction(int transactionId) {
        String query = "UPDATE tr_transaction SET TransactionStatus = 'Finished' WHERE TransactionId = ?";
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            if (ps == null) return;
            ps.setInt(1, transactionId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
