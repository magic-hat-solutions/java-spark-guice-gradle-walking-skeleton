package uk.gov.justice.digital.noms;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import uk.gov.justice.digital.noms.injection.People;
import uk.gov.justice.digital.noms.injection.Storage;
import uk.gov.justice.digital.noms.interfaces.Repository;
import uk.gov.justice.digital.noms.interfaces.Service;

import java.util.Optional;

public class Configuration extends AbstractModule {

    @Override
    protected void configure() {

        bind(Service.class).to(People.class);
        bind(Repository.class).to(Storage.class);

        bind(Integer.class).annotatedWith(Names.named("port")).toInstance(Integer.valueOf(Optional.ofNullable(System.getenv("PORT")).orElse("8080")));
    }
}
