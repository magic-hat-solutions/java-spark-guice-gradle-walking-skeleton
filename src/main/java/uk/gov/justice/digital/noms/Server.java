package uk.gov.justice.digital.noms;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import uk.gov.justice.digital.noms.interfaces.Service;

import static spark.Spark.port;
import static uk.gov.justice.digital.noms.helpers.JsonRoute.*;

public class Server {

    public static void main(String[] args) {

        run(new Configuration());
    }

    public static void run(Configuration configuration) {

        Injector injector = Guice.createInjector(configuration);
        Service people = injector.getInstance(Service.class);

        port(injector.getInstance(Key.get(Integer.class, Names.named("port"))));

        getJson("/people", (req, res) -> people.getAllPeople());
        getJson("/person/:id", (req, res) -> people.getPersonById(Integer.valueOf(req.params("id"))));
    }
}
