package uk.gov.justice.digital.noms.interfaces;

import uk.gov.justice.digital.noms.data.Person;
import java.util.Collection;

public interface Repository {

    Person getById(Integer id);

    Collection<Person> getAll();
}
