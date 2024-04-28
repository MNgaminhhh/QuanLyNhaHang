package vn.mn.quanlynhahang.model;

public class NotifUser {
    private String userUid;
    private String notificationContent;
    private String senderName;
    private String timeSent;

    public NotifUser() {
    }

    public NotifUser(String userUid, String notificationContent, String senderName, String timeSent) {
        this.userUid = userUid;
        this.notificationContent = notificationContent;
        this.senderName = senderName;
        this.timeSent = timeSent;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    @Override
    public String toString() {
        return "NotifUser{" +
                "userUid='" + userUid + '\'' +
                ", notificationContent='" + notificationContent + '\'' +
                ", senderName='" + senderName + '\'' +
                ", timeSent='" + timeSent + '\'' +
                '}';
    }
}
