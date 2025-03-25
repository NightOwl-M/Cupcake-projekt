package app.persistence;

import app.entities.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {


    // Vi opretter en liste med ordrer, som tager ordrene fra databasen og putter dem ind i en liste, som til sidst returneres
    public static List<Order> getAllOrders(ConnectionPool connectionPool){
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY date_and_time DESC;";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int orderId = rs.getInt("order_id");
                User user = rs.getObject("user");
                List<ProductLine> productList = rs.getArray("productLine");
                float orderPrice = rs.getFloat("order_price");
                boolean isPaid = rs.getBoolean("is_paid");

                Order order = new Order(orderId, user, productList, orderPrice, isPaid);
                orderList.add(order);

            }



        } catch (SQLException e) {

            e.printStackTrace();

        }

        return orderList;

    }

}
