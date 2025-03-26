package app.controllers;

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

    private static void createUser(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("user_email"); //TODO: Angiv korrekt i html
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        if (password1.equals(password2)) {
            try {
                UserMapper.createuser(email, password1, connectionPool);
                ctx.attribute("message", "Du er nu oprettet med brugernavn: " + email + ". Nu skal du logge på");
                ctx.render("index.html");

            } catch (DatabaseException e) {
                ctx.attribute("message", "Dit brugernavn findes allerede! Prøv igen eller log ind");
                ctx.render("createuser.html");
            }

        } else {
            ctx.attribute("message", "Dine to password matcher ikke! Prøv igen"); //Vi sender en besked til vores th:text="${message}" på html-siden
            ctx.render("createuser.html");
        }
    }
}