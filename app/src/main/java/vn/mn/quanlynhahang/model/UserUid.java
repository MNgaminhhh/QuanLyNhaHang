package vn.mn.quanlynhahang.model;

public class UserUid {
    private String userId;
    private String avatarurl;
    private String phone;
    private String fullname;
    private String birthdate;
    private String role;
    private String gender;
    private boolean isSelected;

    public UserUid() {
    }

    public UserUid(String userId, String avatarurl, String phone, String fullname, String birthdate, String role, String gender) {
        this.userId = userId;
        this.avatarurl = avatarurl;
        this.phone = phone;
        this.fullname = fullname;
        this.birthdate = birthdate;
        this.role = role;
        this.gender = gender;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    @Override
    public String toString() {
        return "UserUid{" +
                "userId='" + userId + '\'' +
                ", avatarurl='" + avatarurl + '\'' +
                ", phone='" + phone + '\'' +
                ", fullname='" + fullname + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", role='" + role + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
