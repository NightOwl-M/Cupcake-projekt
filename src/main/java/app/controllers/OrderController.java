package app.controllers;


import app.entities.User;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import app.entities.Order;
import app.exceptions.DatabaseException;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

import java.util.List;

public class OrderController {
    // I denne addRoutes metode håndterer vi alle "Handelsrelateret funktioner.
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        //app.get("/", ctx -> backetpage(ctx, connectionPool));
    }

    private static void getOrderById(Context ctx, ConnectionPool connectionPool) {

        User user = ctx.sessionAttribute("currentUser");

        try {

            int orderId = Integer.parseInt(ctx.formParam("taskId"));
            Order order = OrderMapper.getOrderById(orderId, connectionPool);
            ctx.attribute("order", order);
            ctx.render("editorder.html"); // Ved ikke hvilken HTML den skal renderes på

        } catch (NumberFormatException | DatabaseException exception) {

            ctx.attribute("message", "Der gik noget galt");
            ctx.render("index.html");

        }

    }

    private static void deleteOrder(Context ctx, ConnectionPool connectionPool) {
        User user = ctx.sessionAttribute("currentUser");

        try {

            int orderId = Integer.parseInt(ctx.formParam("orderId"));
            OrderMapper.deleteOrder(orderId, connectionPool);
            List<Order> orderList = OrderMapper.getAllOrders(user.getId(), connectionPool);
            ctx.attribute("orderlist", orderList); //Slettes, så snart at task.html er renderent og frigører memory
            ctx.render("orderlist.html");

        } catch (NumberFormatException | DatabaseException exception) {

            ctx.attribute("message", "Der gik noget galt");
            ctx.render("index.html");

        }

    }

}
