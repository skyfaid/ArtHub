package utils;

public class SessionManager {
    private static SessionManager instance;
    private int currentUserId;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public int getCurrentUserId() {
        return this.currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }



}
