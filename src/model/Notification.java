package model;

import java.sql.Timestamp;

public class Notification {
	private int id;
    private int recipientId;
    private String message;
    private Timestamp createdAt;
    private boolean isRead;

    public Notification(int id, int recipientId, String message, Timestamp createdAt, boolean isRead) {
        this.id = id;
        this.recipientId = recipientId;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

	public int getId() {
		return id;
	}

	public int getRecipientId() {
		return recipientId;
	}

	public String getMessage() {
		return message;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public boolean isRead() {
		return isRead;
	}
    
    
}
