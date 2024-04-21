package vn.mn.quanlynhahang.model;

public class User {
    private String avatarurl;
    private String phone;
    private String fullname;
    private String birthdate;
    private String role;
    private String gender;

    public User() {
    }

    public User(String avatarurl, String phone, String fullname, String birthdate, String role, String gender) {
        this.avatarurl = avatarurl;
        this.phone = phone;
        this.fullname = fullname;
        this.birthdate = birthdate;
        this.role = role;
        this.gender = gender;
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
