package app.persistence;

import java.sql.ResultSet;
import app.entities.Order;
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

}
