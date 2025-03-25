package app.entities;

public class User {

    private int userId;

    private String userName;

    private String email;

    private String password;

    private int balance;

    private String role;

    public User(int userId, String userName, String email, String password, int balance, String role) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getBalance() {
        return balance;
    }

    public String getRole() {
        return role;
    }
}
