package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT id, name, balance, isAdmin FROM users WHERE email = ? AND password = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            email,
                            password,
                            rs.getFloat("balance"),
                            rs.getBoolean("isAdmin")
                    );
                } else {
                    throw new DatabaseException("Fejl i login. Prøv igen.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl");
        }
    }

    public static float getBalanceByUserId(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT balance FROM users WHERE user_id = ?";
        float balance = 0;
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                balance = rs.getFloat("balance");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error in getting user with userId = " + userId, e.getMessage());
        }
        return balance;
    }

    public static boolean pay(int userId, float amount, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE users SET balance = ? WHERE user_id = ?";
        boolean updateSuccessful = false;
        float currentBalance = getBalanceByUserId(userId, connectionPool);

        if (currentBalance >= amount) {
            float newBalance = currentBalance - amount;
            try (
                    Connection connection = connectionPool.getConnection();
                    PreparedStatement ps = connection.prepareStatement(sql)
            ) {
                ps.setFloat(1, newBalance);
                ps.setInt(2, userId);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected == 1) {
                    updateSuccessful = true;
                } else {
                    throw new DatabaseException("Error in updating the balance");
                }
            } catch (SQLException e) {
                throw new DatabaseException("Error in updating the balance");
            }
        } else {
            throw new DatabaseException("Insufficient funds");
        }
        return updateSuccessful;
    }
}
