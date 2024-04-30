package vn.mn.quanlynhahang.model;

import com.google.gson.annotations.SerializedName;

public class NotificationRequestBody {
    private String to;
    private NotificationData notification;

    public NotificationRequestBody() {
    }

    public NotificationRequestBody(String to, NotificationData notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationData getNotification() {
        return notification;
    }

    public void setNotification(NotificationData notification) {
        this.notification = notification;
    }
}
