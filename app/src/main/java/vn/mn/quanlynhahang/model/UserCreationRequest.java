package vn.mn.quanlynhahang.model;

public class UserCreationRequest {
    private String email;
    private String password;
    private User userData;

    public UserCreationRequest(String email, String password, User userData) {
        this.email = email;
        this.password = password;
        this.userData = userData;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        this.userData = userData;
    }
}
