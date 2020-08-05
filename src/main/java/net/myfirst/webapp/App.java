package net.myfirst.webapp;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {

    static Map<String, Object> map = new HashMap<>();
    static List<String> users = new ArrayList<>();

        public static void main(String[] args) {
        staticFiles.location("public");

        get("/hello", (request, response) -> new ModelAndView(map, "hello.handlebars"), new HandlebarsTemplateEngine());


        post("/hello", (request, response) -> {

            // create the greeting message
            String user = request.queryParams("username");
            String lang = request.queryParams("language");

            if(!users.contains(user)){
                users.add(user);
            }

            String greet = "";

                if (lang.equals("IsiXhosa")) {
                    greet = "Molo " + user;
                } else if (lang.equals("English")) {
                    greet = "Hello " + user;
                } else if (lang.equals("Afrikaans")) {
                    greet = "Hallo " + user;
                }


            String greeting = greet;
            int counter = users.size();

            // put it in the map which is passed to the template - the value will be merged into the template
            map.put("greeting", greeting);
            map.put("users", users);
            map.put("counter", counter);
            return new ModelAndView(map, "hello.handlebars");

        }, new HandlebarsTemplateEngine());

    }

}
