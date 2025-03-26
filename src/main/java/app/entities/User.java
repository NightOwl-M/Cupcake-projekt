package app.entities;

public class User {
    private int id;
    private String email;
    private String password;
    private float balance;
    private boolean isAdmin;

    public User(int id, String email, String password, float balance, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.isAdmin = isAdmin;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public float getBalance() { return balance; }
    public boolean isAdmin() { return isAdmin; }

    public void setBalance(float balance) { this.balance = balance; }
    public void setAdmin(boolean admin) { isAdmin = admin; }
}
