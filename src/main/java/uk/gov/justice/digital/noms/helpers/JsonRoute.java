package uk.gov.justice.digital.noms.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.get;

public class JsonRoute {

    public static void getJson(String path, Route route) {

        ObjectMapper mapper = new ObjectMapper();

        get(path, (Request req, Response res) -> {

            Object result = route.handle(req, res);

            if (result != null) {
                res.type("application/json");
            } else {
                res.status(404);
            }

            return result;

        }, mapper::writeValueAsString);
    }
}
