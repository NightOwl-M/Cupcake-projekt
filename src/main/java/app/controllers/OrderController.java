package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;

public class OrderController {
    // I denne addRoutes metode håndterer vi alle "Handelsrelateret funktioner.
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        //app.get("/", ctx -> backetpage(ctx, connectionPool));
    }
}
