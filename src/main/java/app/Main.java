package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.UserController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);


        app.get("/", ctx -> ctx.render("index.html"));
        //UserController.addRoutes(app, connectionPool);
    }
}
