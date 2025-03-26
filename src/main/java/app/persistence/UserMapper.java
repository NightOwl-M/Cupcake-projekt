package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT user_id, email, user_password, balance, is_admin FROM users WHERE email = ? AND user_password = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            email,
                            password, // Ingen hashing = hviilket betyder ingen kryptering af password :)
                            rs.getFloat("balance"),
                            rs.getBoolean("is_admin")
                    );
                } else {
                    throw new DatabaseException("Fejl i login. Prøv igen.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl");
        }
    }

    public static void createUser(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO users (email, user_password) VALUES (?, ?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password); // Ingen hashing

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Brugeren kunne ikke oprettes.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl");
        }
    }

    public static List<User> getAllUsers(ConnectionPool connectionPool) throws DatabaseException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, email, balance, is_admin FROM users";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("email"),
                        "HIDDEN", // Skjul adgangskoden
                        rs.getFloat("balance"),
                        rs.getBoolean("is_admin")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database fejl: " + e.getMessage());
        }
        return users;
    }

    public static boolean updateBalance(int userId, float amount, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE users SET balance = balance - ? WHERE user_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setFloat(1, amount);
            ps.setInt(2, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected == 1;
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved opdatering af balance: " + e.getMessage());
        }
    }
}