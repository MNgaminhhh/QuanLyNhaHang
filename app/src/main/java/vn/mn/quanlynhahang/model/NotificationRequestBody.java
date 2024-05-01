package vn.mn.quanlynhahang.model;

import com.google.gson.annotations.SerializedName;

public class NotificationRequestBody {
    private String to;
    private NotificationD data;
    private NotificationData notification;

    public NotificationRequestBody() {
    }

    public NotificationRequestBody(String to, NotificationD data, NotificationData notification) {
        this.to = to;
        this.data = data;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationD getData() {
        return data;
    }

    public void setData(NotificationD data) {
        this.data = data;
    }

    public NotificationData getNotification() {
        return notification;
    }

    public void setNotification(NotificationData notification) {
        this.notification = notification;
    }
}
