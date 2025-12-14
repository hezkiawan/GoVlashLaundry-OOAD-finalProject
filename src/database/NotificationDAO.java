package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.Notification;

public class NotificationDAO {
	private Connect connect = Connect.getInstance();

    // CREATE NOTIFICATION WHEN ADMIN SEND NOTI
    public void createNotification(int recipientId, String message) {
        String query = "INSERT INTO tr_notification (RecipientId, NotificationMessage, CreatedAt, IsRead) VALUES (?, ?, NOW(), 0)";
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            if (ps == null) return;
            ps.setInt(1, recipientId);
            ps.setString(2, message);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // VIEW FOR CUSTOMER
    public Vector<Notification> getNotifications(int recipientId) {
        Vector<Notification> notes = new Vector<>();
        String query = "SELECT * FROM tr_notification WHERE RecipientId = ? ORDER BY CreatedAt DESC";
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            if (ps == null) return notes;
            ps.setInt(1, recipientId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                notes.add(new Notification(
                    rs.getInt("NotificationId"),
                    rs.getInt("RecipientId"),
                    rs.getString("NotificationMessage"),
                    rs.getTimestamp("CreatedAt"),
                    rs.getBoolean("IsRead")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return notes;
    }

    // MARK AS READ
    public void markAsRead(int notificationId) {
        String query = "UPDATE tr_notification SET IsRead = 1 WHERE NotificationId = ?";
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            if (ps == null) return;
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public void deleteNotification(int notificationId) {
        String query = "DELETE FROM tr_notification WHERE NotificationId = ?";
        PreparedStatement ps = connect.prepareStatement(query);
        try {
            if (ps == null) return;
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
