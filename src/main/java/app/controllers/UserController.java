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
        app.get("/login", ctx -> ctx.render("LoginPage.html"));
        app.post("/login", ctx -> login(ctx, connectionPool));
        app.get("/create-user", ctx -> ctx.render("createuser.html"));
        app.post("/create-user", ctx -> createUser(ctx, connectionPool));
        app.get("/logout", UserController::logout);
      //  app.get("/createorder", ctx -> ctx.render("CreateOrder.html"));


    }

    public static void login(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            ctx.attribute("message", "Email og password må ikke være tomme!");
            ctx.render("LoginPage.html");
            return;
        }

        try {
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            ctx.redirect("/viewhistory2");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("LoginPage.html");
        }
    }

    public static void createUser(Context ctx, ConnectionPool connectionPool) {
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
            ctx.render("loginpage.html");
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

    public static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/login?logout=true"); // Her kommer der en pop-up med besked om man er sikker på logout.
    }

}
