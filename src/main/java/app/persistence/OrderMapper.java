package app.persistence;

import java.sql.ResultSet;
import app.entities.Order;
import app.exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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

    public static List<Order> getOrdersByUser(int userId, ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "select * from orders where user_id = ? order by email";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("order_id");
                int userIdentification = rs.getInt("user_id");
                float price = rs.getFloat("order_price");
                boolean isPaid = rs.getBoolean("paid_status");
                orderList.add(new Order(id, userIdentification, price, isPaid));

            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve orders from user...", e.getMessage());
        }
        return orderList;
    }

}
