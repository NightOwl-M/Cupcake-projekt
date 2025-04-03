package app.persistence;

import java.sql.ResultSet;

import app.entities.Bottom;
import app.entities.Order;
import app.entities.ProductLine;
import app.entities.Topping;
import app.exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrderMapper {
    // Denne linje henter en ordre fra databasen baseret på orderId.
    public static Order getOrderById(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        //Denne linje er en SQL-forspørgelse. Det er ligesom at forklare ismanden hvilke kugler du vil have på din isvaffel.
        String sql = "SELECT * FROM orders o " +
                "JOIN productline p ON o.order_id = p.order_id " +
                "JOIN bottom b ON p.bottom_id = b.bottom_id " +
                "JOIN topping t ON p.topping_id = t.topping_id " +
                "WHERE o.order_id = ?";
        // Denne linje sikre at forbindelsen lukkes automatisk og forhindrer SQL-injektion og optimere performance ved at genbrug af kald.
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            // Denne linje binder det første ord "orderId" til det første (?) i SQL'en.
            ps.setInt(1, orderId);
            // denne linje udfører forspørgelsen og returnerer et "resultSet" (database-rækker).
            ResultSet rs = ps.executeQuery();
            // Denne linje tjekker for ordre findes.
            if (rs.next()) {
                //læser ordrehoveddata fra den første række (Order) også opretter et Order-objekt af disse data.
                int userId = rs.getInt("user_id");
                float orderPrice = rs.getFloat("order_price");
                boolean isPaid = rs.getBoolean("paid_status");
                Order order = new Order(orderId, userId, orderPrice, isPaid);

                // Denne linje opretter vi en tom ArrayList til at holde på alle productLine for ordren.
                List<ProductLine> productLines = new ArrayList<>();
                // Denne linje laver vi "do-while-loop" hvor "do" sikrer at den første række behandles. altså rs.next()
                do {

                    int bottomId = rs.getInt("bottom_id"); // int f.eks "1".
                    String bottomName = rs.getString("bottom_name"); // String f.eks "Chokolade".
                    float bottomPrice = rs.getFloat("bottom_price"); // float f.eks 22,2
                    int toppingId = rs.getInt("topping_id"); // int f.eks "3 eller 0 hvis NULL"
                    String toppingName = rs.getString("topping_name"); // String f.eks "Cream eller NULL)
                    float toppingPrice = rs.getFloat("topping_price"); // float f.eks 10.0 eller 0.0
                    // Oprettelse af ProductLine: for hver række oprettes et productLine-objekt med disse data.
                    ProductLine productLine = new ProductLine(bottomId, bottomName, bottomPrice, toppingId, toppingName, toppingPrice);
                    productLines.add(productLine);
                } while (rs.next());

                order.setProductLines(productLines);
                return order;
            } else {
                throw new DatabaseException("Ordre ikke fundet med ID: " + orderId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af ordre: " + e.getMessage());
        }
    }

    public static void deleteOrder(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved at slettet orderen: Ingen rækker er påvirket");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved at slettet orderen", e);
        }
    }

    public static Order createOrder(int userId, float orderPrice, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (user_id, order_price, paid_status) VALUES (?, ?, ?)";
        Order newOrder = null;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setFloat(2, orderPrice);
            ps.setBoolean(3, false);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                // Hent den genererede order_id
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int orderId = rs.getInt(1);
                        newOrder = new Order(orderId, userId, orderPrice, false);
                    }
                }
            } else {
                throw new DatabaseException("Fejl ved oprettelse af ordre.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved oprettelse af ordre: " + e.getMessage());
        }
        return newOrder;
    }

    public static List<Order> getOrdersByUser(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        List<Order> orders = new ArrayList<>();


        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                float orderPrice = rs.getFloat("order_price");
                boolean isPaid = rs.getBoolean("paid_status");
                orders.add(new Order(orderId, userId, orderPrice, isPaid));
            }
            for (Order order: orders) {
                order.setProductLines(getProductLineByOrderId(order.getOrderId(), connectionPool));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching user orders: " + e.getMessage());
        }
        return orders;
    }

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
            String sql = "SELECT * FROM orders";
            List<Order> orders = new ArrayList<>();

            try (Connection connection = connectionPool.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int userId = rs.getInt("user_id");
                    float orderPrice = rs.getFloat("order_price");
                    boolean isPaid = rs.getBoolean("paid_status");
                    orders.add(new Order(orderId, userId, orderPrice, isPaid));
                }
            } catch (SQLException e) {
                throw new DatabaseException("Error fetching all orders: " + e.getMessage());
            }
            return orders;
        }

    public static List<ProductLine> getProductLineByOrderId(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM orders o \n" +
                "JOIN productline p ON o.order_id = p.order_id \n" +
                "JOIN bottom b ON p.bottom_id = b.bottom_id \n" +
                "JOIN topping t ON p.topping_id = t.topping_id \n" +
                "WHERE o.order_id = ?";
        List<ProductLine> productLineList = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int bottomId = rs.getInt("bottom_id");
                String bottomName = rs.getString("bottom_name");
                float bottomPrice = rs.getFloat("bottom_price");
                int toppingId = rs.getInt("topping_id");
                String toppingName = rs.getString("topping_name");
                float toppingPrice = rs.getFloat("topping_price");
                int quantity = rs.getInt("quantity");


                // Oprettelse af ProductLine: for hver række oprettes et productLine-objekt med disse data.
                ProductLine productLine = new ProductLine(bottomId, bottomName, bottomPrice, toppingId, toppingName, toppingPrice, quantity);
                productLineList.add(productLine);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error in getting productlines with orderId = " + orderId, e.getMessage());
        }
        return productLineList;
    }

    public static boolean setOrderStatus(int orderId, boolean isPaid, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE orders SET paid_status = ? WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, isPaid);
            ps.setInt(2, orderId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected == 1;
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl");
        }
    }

    public static boolean setOrderPrice(int orderId, float orderPrice, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE orders SET order_price = ? WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setFloat(1, orderPrice);
            ps.setInt(2, orderId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected == 1;
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl");
        }
    }



    public static void addProductLine(int bottomId, Integer toppingId, int orderId, int quantity, float totalPrice, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO productline (bottom_id, topping_id, order_id, quantity, total_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bottomId);
            ps.setObject(2, toppingId);
            ps.setInt(3, orderId);
            ps.setInt(4, quantity);
            ps.setFloat(5, totalPrice);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indsættelse af produktlinje: " + e.getMessage());
        }
    }

    public static float getBottomPriceById(int id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT bottom_price FROM bottom WHERE bottom_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("bottom_price");
            } else {
                throw new DatabaseException("Produkt ikke fundet med ID: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af pris: " + e.getMessage());
        }
    }

    public static float getToppingPriceById(int id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT topping_price FROM topping WHERE topping_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("topping_price");
            } else {
                throw new DatabaseException("Produkt ikke fundet med ID: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af pris: " + e.getMessage());
        }
    }

    public static List<Bottom> getAllBottoms(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM bottom";
        List<Bottom> allBottoms = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int bottomId = rs.getInt("bottom_id");
                String bottomName = rs.getString("bottom_name");
                float bottomPrice = rs.getFloat("bottom_price");

                allBottoms.add(new Bottom(bottomId, bottomName, bottomPrice));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all orders: " + e.getMessage());
        }
        return allBottoms;
    }

    public static List<Topping> getAllToppings(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM topping";
        List<Topping> allToppings = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int toppingId = rs.getInt("topping_id");
                String toppingName = rs.getString("topping_name");
                float toppingPrice = rs.getFloat("topping_price");

                allToppings.add(new Topping(toppingId, toppingName, toppingPrice));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all orders: " + e.getMessage());
        }
        return allToppings;
    }

    public static List<Order> getAllOrdersWithUserDetails(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT o.order_id, o.user_id, o.order_price, o.paid_status, " +
                "u.email AS user_email, " +
                "p.productline_id, p.quantity, p.total_price, " +
                "b.bottom_id, b.bottom_name, b.bottom_price, " +
                "t.topping_id, t.topping_name, t.topping_price " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "LEFT JOIN productline p ON o.order_id = p.order_id " +
                "LEFT JOIN bottom b ON p.bottom_id = b.bottom_id " +
                "LEFT JOIN topping t ON p.topping_id = t.topping_id " +
                "ORDER BY o.order_id, p.productline_id";

        List<Order> orders = new ArrayList<>();
        Order currentOrder = null;
        int lastOrderId = -1;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");

                if (orderId != lastOrderId) {
                    currentOrder = new Order(
                            orderId,
                            rs.getInt("user_id"),
                            rs.getFloat("order_price"),
                            rs.getBoolean("paid_status")
                    );
                    currentOrder.setUserEmail(rs.getString("user_email"));
                    orders.add(currentOrder);
                    lastOrderId = orderId;
                }

                if (rs.getObject("productline_id") != null) {
                    ProductLine pl = new ProductLine(
                            rs.getInt("bottom_id"),
                            rs.getString("bottom_name"),
                            rs.getFloat("bottom_price"),
                            rs.getInt("topping_id"),
                            rs.getString("topping_name"),
                            rs.getFloat("topping_price"),
                            rs.getInt("quantity")
                    );
                    pl.setTotalPrice(rs.getFloat("total_price"));
                    currentOrder.addProductLine(pl);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all orders with user details: " + e.getMessage());
        }

        return orders;
    }
}