package controller;

import java.util.Vector;

import database.NotificationDAO;
import model.Notification;

public class NotificationController {
	private NotificationDAO noteDAO = new NotificationDAO();

    // SEND NOTIFICATION (Admin)
    public String sendNotification(int recipientId) {
        if (recipientId == 0) return "Recipient Invalid.";
        
        String message = "Your order is finished and ready for pickup. Thank you for choosing our service!";
        
        noteDAO.createNotification(recipientId, message);
        return "Success";
    }

    // GET NOTIFICATIONS (Customer)
    public Vector<Notification> getMyNotifications(int userId) {
        return noteDAO.getNotifications(userId);
    }
    
    // READ NOTIFICATION (Customer)
    public void markAsRead(int notificationId) {
        noteDAO.markAsRead(notificationId);
    }
    
    public void deleteNotification(int notificationId) {
        noteDAO.deleteNotification(notificationId);
    }
}
