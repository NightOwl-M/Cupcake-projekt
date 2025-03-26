package app.persistence;

import java.sql.ResultSet;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import java.sql.*;


public class OrderMapper {

    public static Order getOrderById(int orderId, ConnectionPool connectionPool) throws DatabaseException {

        Order order = null;
        String sql = "select * from order where order_id = ?";

        try (

                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ){

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int id = rs.getInt("order_id");
                int userId = rs.getInt("user_id");
                int price = rs.getInt("order_price");
                boolean status = rs.getBoolean("paid_status");
                order = new Order(id, userId, price, status);

            }

        } catch (SQLException e) {

            throw new DatabaseException("Failed to find a order with following ID: " + orderId+", "+ e.getMessage());

        }

        return order;

    }

    public static void deleteOrder(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "delete from order where order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Failed to update table...");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete order", e.getMessage());
        }
    }

    public static Order createOrder(int userId, float orderPrice, boolean isPaid, ConnectionPool connectionPool) throws DatabaseException {
        Order newOrder = null;

        String sql = "INSERT INTO orders (order_price, paid_status, user_id) VALUES (?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setFloat(1, orderPrice);
            ps.setBoolean(2, isPaid);
            ps.setInt(3, userId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int newOrderId = rs.getInt(1);
                newOrder = new Order(newOrderId, userId, orderPrice, isPaid);
            } else {
                throw new DatabaseException("Error when inserting order");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error when inserting order", e.getMessage());
        }
        return newOrder;
    }
}
