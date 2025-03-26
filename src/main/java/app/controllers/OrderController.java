package app.controllers;


import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
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

    public static void createOrder(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser"); //TODO angiv korrekt i html
        try {
            float orderPrice = Float.parseFloat(ctx.formParam("total_price")); //TODO angiv korrekt i html
            boolean isPaid;

            if (UserMapper.updateBalance(currentUser.getId(), orderPrice)){
                isPaid = true;
                Order newOrder = OrderMapper.createOrder(currentUser.getId(), orderPrice, isPaid);
                ctx.attribute("message", "Ordre gennemført");
                ctx.attribute("newOrder", newOrder); //TODO angiv korrekt i html
                ctx.render("order_complete.html"); //TODO angiv korrekt i html
            } else {
                ctx.attribute("message", "Noget gik galt prøv igen");
                ctx.render("basket.html"); //TODO angiv korrekt i html
            }
        } catch (DatabaseException e) {
            ctx.attribute("message", "Noget gik galt, prøv igen");
            ctx.render("basket.html"); //TODO angiv korrekt i html
        }
    }
}