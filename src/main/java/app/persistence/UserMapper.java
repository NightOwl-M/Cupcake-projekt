package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    public static List<User> getAllUsers(ConnectionPool connectionPool) throws DatabaseException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, email, user_password, balance, is_admin FROM public.users";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("user_id");
                    String email = rs.getString("email");
                    String password = rs.getString("user_password");
                    float balance = rs.getFloat("balance");
                    boolean isAdmin = rs.getBoolean("is_admin");

                    User user = new User(id, email, password, balance, isAdmin);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("No users found...");
        }
        return users;
    }

}
