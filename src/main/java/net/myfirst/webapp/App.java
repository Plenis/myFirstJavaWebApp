package net.myfirst.webapp;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static spark.Spark.*;

public class App {

    static Map<String, Object> map = new HashMap<>();
    static List<String> users = new ArrayList<>();

        public static void main(String[] args) {
            staticFiles.location("public");
            port(getHerokuAssignedPort());

            AtomicReference<String> greet = new AtomicReference<>("");

            get("/hello", (request, response) -> new ModelAndView(map, "hello.handlebars"), new HandlebarsTemplateEngine());


            post("/hello", (request, response) -> {

                // create the greeting message
                String user = request.queryParams("username");
                String lang = request.queryParams("language");

                if (!users.contains(user)) {
                    users.add(user);
                }


                if (lang.equals("IsiXhosa")) {
                    greet.set("Molo " + user);
                } else if (lang.equals("English")) {
                    greet.set("Hello " + user);
                } else if (lang.equals("Afrikaans")) {
                    greet.set("Gooi dag " + user);
                }

                String greeting = greet.get();
                int counter = users.size();

                // put it in the map which is passed to the template - the value will be merged into the template
                map.put("greeting", greeting);
                map.put("users", users);
                map.put("counter", counter);
                return new ModelAndView(map, "hello.handlebars");

            }, new HandlebarsTemplateEngine());

//            get("/greeted/:username", (request, response) -> new ModelAndView(map, "greeted.handlebars"), new HandlebarsTemplateEngine());
//
//            post("/greeted/:username", (request, response) -> {
//                String username = request.queryParams("username");
////               String uniqueCounter = request.queryParams("uniqueCounter");
//
//                int uniqueCounter = 0;
//
//                if(map.isEmpty()){
//                    new ModelAndView("greeting", "Name not available!");
//                }
//                else if(map.containsKey(username)){
//                    uniqueCounter = (int) map.get(username);
//                }
//                map.put("username", username);
//                map.put("uniqueCounter", uniqueCounter);
//                return new ModelAndView(map, "greeted.handlebars");
//            }, new HandlebarsTemplateEngine());
        }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

}
