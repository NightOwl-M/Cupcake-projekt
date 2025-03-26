package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;



public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/login", ctx -> login(ctx, connectionPool));
        app.post("/create-user", ctx -> createUser(ctx, connectionPool));
        app.get("/users", ctx -> getAllUsers(ctx, connectionPool));
    }

    public static void login(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            ctx.attribute("message", "Email og password må ikke være tomme!");
            ctx.render("HTML.html");
            return;
        }

        try {
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            ctx.render("/HTML1.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("HTML.html");
        }
    }

    private static void createUser(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("user_email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        if (email == null || email.isEmpty() || password1 == null || password1.isEmpty()) {
            ctx.attribute("message", "Email og password må ikke være tomme!");
            ctx.render("createuser.html");
            return;
        }

        if (!password1.equals(password2)) {
            ctx.attribute("message", "Dine to passwords matcher ikke!");
            ctx.render("createuser.html");
            return;
        }

        try {
            UserMapper.createUser(email, password1, connectionPool);
            ctx.attribute("message", "Du er nu oprettet.");
            ctx.render("index.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("createuser.html");
        }
    }

    public static void getAllUsers(Context ctx, ConnectionPool connectionPool) {
        try {
            List<User> userList = UserMapper.getAllUsers(connectionPool);
            ctx.attribute("userList", userList);
            ctx.render("user_list.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("error.html");
        }
    }
}
