package uk.gov.justice.digital.noms;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import spark.Request;
import spark.Response;
import spark.Route;
import uk.gov.justice.digital.noms.interfaces.Service;

import static spark.Spark.*;
import static uk.gov.justice.digital.noms.helpers.JsonRoute.*;

// Test server only has Java 1.7, not 1.8, had to change all Spark lambdas to anonymous classes

public class Server {

    public static void main(String[] args) {

        run(new Configuration());
    }

    public static void run(Configuration configuration) {

        Injector injector = Guice.createInjector(configuration);
        final Service people = injector.getInstance(Service.class);

        setPort(injector.getInstance(Key.get(Integer.class, Names.named("port"))));

        staticFileLocation("public");

        getJson("/people/:surname/:forename", new Route("") {
            @Override
            public Object handle(Request req, Response res) {

                return people.findPerson(req.params("forename"), req.params("surname"));
            }
        });
    }
}
