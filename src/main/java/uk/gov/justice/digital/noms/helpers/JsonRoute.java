package uk.gov.justice.digital.noms.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.ResponseTransformerRoute;
import spark.Route;

import static spark.Spark.get;

public class JsonRoute {

    public static void getJson(String path, final Route route) {

        final ObjectMapper mapper = new ObjectMapper();

        get(new ResponseTransformerRoute(path) {
            @Override
            public Object handle(Request req, Response res) {
                Object result = route.handle(req, res);

                if (result != null) {
                    res.type("application/json");
                } else {
                    res.status(404);
                }

                return result;
            }

            @Override
            public String render(Object model) {
                try {

                    return mapper.writeValueAsString(model);

                } catch (JsonProcessingException ex) {

                    return ex.getMessage();
                }

            }
        });
    }
}
