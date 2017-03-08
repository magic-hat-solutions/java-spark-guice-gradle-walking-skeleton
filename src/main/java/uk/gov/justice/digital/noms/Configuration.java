package uk.gov.justice.digital.noms;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import uk.gov.justice.digital.noms.injection.People;
import uk.gov.justice.digital.noms.interfaces.Service;

public class Configuration extends AbstractModule {

    @Override
    protected void configure() {

        bind(Service.class).to(People.class);

        bind(Integer.class).annotatedWith(Names.named("port")).toInstance(Integer.valueOf(Optional.fromNullable(System.getenv("PORT")).or("8080")));
    }
}
