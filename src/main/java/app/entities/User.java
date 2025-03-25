package app.entities;

public class User {
    private int userId;
    private String username;
    private String password;
    private float balance;
    private boolean isAdmin;

    public User(int userId, String username, String password, float balance, boolean isAdmin) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public float getBalance() {
        return balance;
    }

    public boolean pay(float amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }

}
