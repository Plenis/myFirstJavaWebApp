package net.myfirst.webapp;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        String dbDiskURL = "jdbc:h2:file:./greetdb";

        Jdbi jdbi = Jdbi.create(dbDiskURL, "sa", "");

        // get a handle to the database
        Handle handle = jdbi.open();

        // create the table if needed
        handle.execute("create table if not exists greet ( id integer identity, name varchar(50), counter int )");

        staticFiles.location("public");
        port(getHerokuAssignedPort());

        AtomicReference<String> greet = new AtomicReference<>("");

        get("/", (req, res) -> {

            res.redirect("/hello");
            return "";
        });


        get("/hello", (request, response) -> {
            List<String> users = handle.createQuery("select name from greet")
                    .mapTo(String.class)
                    .list();

            Map<String, Object> map = new HashMap<>();

            map.put("counter", users.size());
            map.put("username", users);


            return new ModelAndView(map, "hello.handlebars");
        }, new HandlebarsTemplateEngine());

        post("/hello", (request, response) -> {
            Map<String, Object> map = new HashMap<>();

            String user = request.queryParams("username");
            String lang = request.queryParams("language");

                // check if a user exists
                int count = handle.select("select count(*) from greet where name = ?", user)
                        .mapTo(int.class)
                        .findOnly();

                if (count == 0) {
                    // add a user to the database
                    handle.execute("insert into greet (name) values (?)", user);
                }

            // get all the usernames from the database
            List<String> users = handle.createQuery("select name from greet")
                    .mapTo(String.class)
                    .list();

            switch (lang) {
                case "IsiXhosa":
                    greet.set("Molo " + user);
                    break;
                case "English":
                    greet.set("Hello, " + user);
                    break;
                case "Afrikaans":
                    greet.set("Gooi dag, " + user);
                    break;
            }

            String greeting = greet.get();
            int counter = users.size();

            int uniqueCounter = 0;

            // put it in the map which is passed to the template - the value will be merged into the template
            map.put("greeting", greeting);
            map.put("users", users);
            map.put("counter", counter);
            map.put("username", user);
            map.put("uniqueCounter", uniqueCounter);
            return new ModelAndView(map, "hello.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/greeted/:username", (request, response) -> {
            Map<String, Object> map = new HashMap<>();

            return new ModelAndView(map, "greeted.handlebars");
        }, new HandlebarsTemplateEngine());

//            post("/greeted/:username", (request, response) -> {
//                String username = request.queryParams("username");
//               String uniqueCounter = request.queryParams("uniqueCounter");

//                int uniqueCounter = 0;
//                        users.get(username).toString();
//
//                if(username.isEmpty()){
//                    new ModelAndView("greeting", "Name not available!");
//                }
//                else if(map.containsKey(username)){
//                    uniqueCounter++;
//                }
//                map.put("username", username);
//                map.put("uniqueCounter", uniqueCounter);
//                return new ModelAndView(map, "greeted.handlebars");
//            }, new HandlebarsTemplateEngine());

    }

        static int getHerokuAssignedPort () {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (processBuilder.environment().get("PORT") != null) {
                return Integer.parseInt(processBuilder.environment().get("PORT"));
            }
            return 4567;
        }

    }

