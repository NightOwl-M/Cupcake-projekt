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
}
