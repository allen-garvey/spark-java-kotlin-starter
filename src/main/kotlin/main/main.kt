package main

/**
 * Created by allen on 7/28/16.
 */
import spark.Spark.*
import com.google.gson.Gson
import models.User
import spark.ModelAndView
import spark.template.handlebars.HandlebarsTemplateEngine
import java.util.*

fun main(args : Array<String>) {
    port(3000)

    staticFiles.location("/public");

    //gzip everything
    after({req, res ->
        res.header("Content-Encoding", "gzip")
    })

    //used to parse and convert JSON
    val gson = Gson()
    val templateEngine = HandlebarsTemplateEngine()

    get("/", { req, res -> ModelAndView(hashMapOf(Pair("name", "Test")), "index.hbs")  }, templateEngine)
    get("/hello/:name", { req, res -> ModelAndView(hashMapOf(Pair("name", req.params(":name"))), "index.hbs")  }, templateEngine)
    get("/user/:first/:last/json", { req, res -> User(req.params(":first"), req.params(":last")) }, { gson.toJson(it) })
}