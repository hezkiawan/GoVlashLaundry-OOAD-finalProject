package main;

import model.User;

public class UserSession {
	private static User currentUser;

    public static User getInstance() {
        return currentUser;
    }

    public static void setInstance(User user) {
        currentUser = user;
    }
    
    public static void clear() {
        currentUser = null;
    }
}
