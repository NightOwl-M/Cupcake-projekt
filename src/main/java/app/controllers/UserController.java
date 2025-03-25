package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;

public class UserController {
    // I denne addRoutes metode håndterer vi brugerhåndteringen.
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        //app.post("/login", ctx -> login(ctx, connectionPool));
    }
}
