package app.controllers;

import app.entities.Order;
import app.persistence.OrderMapper;
import io.javalin.http.Context;
import java.util.List;

public class OrderController {

    public static void getAllOrders(Context ctx, ConnectionPool connectionPool) {
        List<Order> orderList = OrderMapper.getAllOrders(connectionPool);

        ctx.attribute("orderList", orderList);
        ctx.render("?.html"); // TODO Tilpas til den rette HTML
    }

}
