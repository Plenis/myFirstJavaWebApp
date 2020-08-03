package net.myfirst.webapp;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        staticFiles.location("public");
        Map<String, Object> map = new HashMap<>();

        get("/hello", (request, response) ->{

//            Map<String, Object> map = new HashMap<>();
            return new ModelAndView(map, "hello.handlebars");

        }, new HandlebarsTemplateEngine());


        post("/hello", (request, response) -> {
            // create the greeting message
            String greeting = "Hello, " + request.queryParams("username");
            String[] users = request.queryParamsValues("username");

            // put it in the map which is passed to the template - the value will be merged into the template
            map.put("greeting", greeting);
            map.put("users", users);
            return new ModelAndView(map, "hello.handlebars");

        }, new HandlebarsTemplateEngine());

//        get("hello", (request, response) ->{
//
////            Map<String, Object> map = new HashMap<>();
//            return map;
//
//        });
//
//        post("hello", (request, response) ->{
//
////            Map<String, Object> map = new HashMap<>();
//
//            String users = request.queryParams(":greeted");
//
//            map.put("users", users);
//            return map;
//        });

//        get("/greet", (request, response) ->
//                "Molo!");
//
//        post("/greet", (request, response) ->
//                "Molo " + request.queryParams("username")
//        );
//
//        get("/greet/:username", (request, response) ->
//                "Molo " + request.params("username"));
//
//        get("/greet/:username/language/:language", ((request, response) ->
//                "Greet user " + request.params("username") + " in " + request.params("language")
//        ));
    }

}
