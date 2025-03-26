package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {
    // I denne addRoutes metode håndterer vi brugerhåndteringen.
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        //app.post("/login", ctx -> login(ctx, connectionPool));
    }

}
