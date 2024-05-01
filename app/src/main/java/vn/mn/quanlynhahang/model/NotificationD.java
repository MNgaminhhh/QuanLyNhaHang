package vn.mn.quanlynhahang.model;

public class NotificationD {
    private String userId;

    public NotificationD() {
    }

    public NotificationD(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
