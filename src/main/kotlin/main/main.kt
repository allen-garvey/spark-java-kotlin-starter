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

    //allow routes to match with trailing slash
    before(Filter({ req, res ->
        val path = req.pathInfo()
        if (!path.equals("/") && path.endsWith("/")){
            res.redirect(path.substring(0, path.length - 1))
        }
    }))

    //set response type to json for api routes
    after(Filter({req, res ->
        if(req.pathInfo().startsWith("/api")){
            res.type("application/json")
        }
    }))

    //gzip everything
    after(Filter({req, res ->
        res.header("Content-Encoding", "gzip")
    }))

    //used to parse and convert JSON
    val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    //used to parse and convert JSON
    val gson = Gson()
    val templateEngine = HandlebarsTemplateEngine()

    get("/", { req, res -> ModelAndView(hashMapOf(Pair("name", "Test")), "index.hbs")  }, templateEngine)
    get("/hello/:name", { req, res -> ModelAndView(hashMapOf(Pair("name", req.params(":name"))), "index.hbs")  }, templateEngine)
    get("api/user/:first/:last", { req, res -> User(req.params(":first"), req.params(":last")) }, { gson.toJson(it) })
}
