package app.controllers;

import app.entities.Bottom;
import app.entities.Topping;
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
      app.get("/CreateOrders", ctx -> ctx.render("CreateOrder.html"));
        app.get("/createorder", ctx -> getAllBottomsAndToppings(ctx, connectionPool));
        app.get("/basket", ctx -> ctx.render("Basket.html"));
        app.post("/addToBasket", ctx -> createOrder(ctx,connectionPool));
        app.post("/pay", ctx -> payOrder(ctx,connectionPool));
        app.get("/continue-shopping", ctx -> ctx.render("CreateOrder.html"));
        app.get("/viewhistory2", ctx -> getOrdersByUser(ctx, connectionPool));
    }

    public static void createOrder(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");
        Order currentOrder = ctx.sessionAttribute("currentOrder");

        try {
            if (currentOrder == null) {
                currentOrder = OrderMapper.createOrder(currentUser.getId(), 0, connectionPool);
                ctx.sessionAttribute("currentOrder", currentOrder);
            }
            int orderId = currentOrder.getOrderId();
            float totalPrice = 0;
            int toppingId = Integer.parseInt(ctx.formParam("topping"));
            int bottomId = Integer.parseInt(ctx.formParam("bottom"));
            int quantity = Integer.parseInt(ctx.formParam("quantity"));

            OrderMapper.addProductLine(bottomId, toppingId, orderId, quantity, totalPrice, connectionPool); //Cupcakes gemmes i DB
            currentOrder.setProductLines(OrderMapper.getProductLineByOrderId(currentOrder.getOrderId(), connectionPool)); //Cupcakes tilføjes til ordrens List<ProductLine>

            ctx.sessionAttribute("currentOrder", currentOrder);
            ctx.redirect("createorder");

        } catch (NumberFormatException e) {
            ctx.sessionAttribute("errorMessage", "Invalid input for order creation.");
            ctx.redirect("createorder");
        } catch (DatabaseException e) {
            ctx.sessionAttribute("errorMessage", "Error creating order: " + e.getMessage());
            ctx.redirect("createorder");
        }
    }

    public static void getAllBottomsAndToppings(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.status(401).result("Not authenticated");
            return;
        }
        try {
            List<Topping> allToppings = OrderMapper.getAllToppings(connectionPool);
            ctx.attribute("allToppings", allToppings);

            List<Bottom> allBottoms = OrderMapper.getAllBottoms(connectionPool);
            ctx.attribute("allBottoms", allBottoms);

            ctx.render("CreateOrder.html");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error fetching bottoms and toppings: " + e.getMessage());
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
            ctx.attribute("orders", orders);
            ctx.render("ViewHistory2.html");
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
        ctx.render("viewhistory2.html");
    }

    public static void payOrder(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");
        Order currentOrder = ctx.sessionAttribute("currentOrder");

        try {
            boolean paymentSuccess = UserMapper.pay(currentUser.getId(), currentOrder.getOrderPrice(), connectionPool);
            if (paymentSuccess) {
                boolean orderStatusUpdateSuccess = OrderMapper.setOrderStatus(currentOrder.getOrderId(), true, connectionPool);
                if (orderStatusUpdateSuccess) {
                    currentOrder.setPaid(true);
                    ctx.sessionAttribute("currentOrder", null); //ordre sættes til null, så man ikke kan tilføje mere til en betalt ordre
                    ctx.redirect("viewhistory2");
                }
            }
        } catch (DatabaseException e) {
            ctx.attribute("message", "Error: Something went wrong with the payment");
            ctx.render("Basket.html");
        }
    }

}
