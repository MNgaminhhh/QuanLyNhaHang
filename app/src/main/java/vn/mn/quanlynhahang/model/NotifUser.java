package vn.mn.quanlynhahang.model;

public class NotifUser {
    private String userUid;
    private String notificationContent;
    private String senderUid;
    private String senderName;

    private String timeSent;
    private boolean isSelected;
    public NotifUser() {
    }

    public NotifUser(String userUid, String notificationContent, String senderUid, String senderName, String timeSent) {
        this.userUid = userUid;
        this.notificationContent = notificationContent;
        this.senderUid = senderUid;
        this.senderName = senderName;
        this.timeSent = timeSent;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    @Override
    public String toString() {
        return "NotifUser{" +
                "userUid='" + userUid + '\'' +
                ", notificationContent='" + notificationContent + '\'' +
                ", senderUid='" + senderUid + '\'' +
                ", senderName='" + senderName + '\'' +
                ", timeSent='" + timeSent + '\'' +
                '}';
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

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
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
}
