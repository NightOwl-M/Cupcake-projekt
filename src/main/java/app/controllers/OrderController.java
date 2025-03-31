package app.controllers;

import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import app.entities.Order;
import app.exceptions.DatabaseException;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderController {
    // I denne addRoutes metode håndterer vi alle "Handelsrelateret funktioner.
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/orders", ctx -> ctx.render("orders.html"));
        app.get("/viewhistory", ctx -> viewHistory(ctx, connectionPool));
        //app.get("/adminorders", ctx -> ctx.render("AdminViewAllOrders.html"));
        app.get("/adminorders", ctx -> getAllOrdersAdmin(ctx, connectionPool));
    }

    public static void createOrder(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");

        try {
            // Hent data fra form
            float orderPrice = Float.parseFloat(ctx.formParam("total_price"));
            List<String> productIds = ctx.formParams("product_ids");
            List<String> toppingIds = ctx.formParams("topping_ids");
            List<String> quantities = ctx.formParams("quantities");

            // Denne if opdater brugerens balance.
            if (!UserMapper.updateBalance(currentUser.getId(), orderPrice, connectionPool)) {
                ctx.attribute("message", "Kunne ikke opdatere balance");
                ctx.render("basket.html");
                return;
            }

            Order newOrder = OrderMapper.createOrder(currentUser.getId(), orderPrice, connectionPool);
            int orderId = newOrder.getOrderId();

            // Tilføj produktlinjer
            for (int i = 0; i < productIds.size(); i++) {
                int bottomId = Integer.parseInt(productIds.get(i));
                Integer toppingId = toppingIds.get(i).isEmpty() ? null : Integer.parseInt(toppingIds.get(i));
                int quantity = Integer.parseInt(quantities.get(i));

                float productPrice = getPriceById(bottomId, connectionPool);
                float toppingPrice = toppingId != null ? getPriceById(toppingId, connectionPool) : 0;
                float totalPrice = (productPrice + toppingPrice) * quantity;

                addProductLine(bottomId, toppingId, orderId, quantity, totalPrice, connectionPool);
            }

            // Send succesbesked til bruger
            ctx.status(200).result("Order created and added to cart.");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid input for order creation.");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error creating order: " + e.getMessage());
        }
    }

    // metode til at hente pris for bund og topping
    public static float getPriceById(int id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT price FROM bottom WHERE bottom_id = ?"; // Juster for topping, hvis nødvendigt
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("price");
            } else {
                throw new DatabaseException("Produkt ikke fundet med ID: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af pris: " + e.getMessage());
        }
    }

    // produktlinje til databasen
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

    //TODO har lavet den her metode. Skal være et PR
    public static void getAllOrdersAdmin(Context ctx, ConnectionPool connectionPool) {
        System.out.println("Getting all orders: ");
        User currentUser = ctx.sessionAttribute("currentUser");
        /*if (currentUser == null || !currentUser.isAdmin()) {
            ctx.status(401).result("Not authenticated");
            return;
        }*/
        try {
            List<Order> orders = OrderMapper.getAllOrders(connectionPool);
            System.out.println("Received orders:");
            System.out.println(orders);
            ctx.attribute("orders", orders);
            ctx.render("AdminViewAllOrders.html");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error fetching all orders: " + e.getMessage());
        }
    }

    public static void getOrdersByUser(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");
        if (currentUser == null) {
            ctx.status(401).result("Not authenticated");
            return;
        }

        try {
            List<Order> orders = OrderMapper.getOrdersByUser(currentUser.getId(), connectionPool);
            ctx.json(orders);
        } catch (DatabaseException e) {
            ctx.status(500).result("Error fetching orders: " + e.getMessage());
        }
    }

    public static void getAllOrders(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Order> orders = OrderMapper.getAllOrders(connectionPool);
            ctx.json(orders);
        } catch (DatabaseException e) {
            ctx.status(500).result("Error fetching all orders: " + e.getMessage());
        }
    }

    public static void getOrderById(Context ctx, ConnectionPool connectionPool) {
        try {
            int orderId = Integer.parseInt(ctx.pathParam("id"));
            Order order = OrderMapper.getOrderById(orderId, connectionPool);
            if (order != null) {
                ctx.json(order);
            } else {
                ctx.status(404).result("Order not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid order ID");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error fetching order: " + e.getMessage());
        }
    }

    public static void deleteOrder(Context ctx, ConnectionPool connectionPool) {
        try {
            int orderId = Integer.parseInt(ctx.pathParam("id"));
            OrderMapper.deleteOrder(orderId, connectionPool);
            ctx.status(200).result("Order deleted successfully");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid order ID");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error deleting order: " + e.getMessage());
        }
    }

    public static void viewHistory(Context ctx, ConnectionPool connectionPool) {
        User user = ctx.sessionAttribute("currentUser");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }
        ctx.attribute("userEmail", user.getEmail());
        ctx.render("ViewHistory.html");
    }
}
