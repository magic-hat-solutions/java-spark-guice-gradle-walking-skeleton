package uk.gov.justice.digital.noms.injection;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;
import uk.gov.justice.digital.noms.data.Person;
import uk.gov.justice.digital.noms.interfaces.Repository;
import uk.gov.justice.digital.noms.interfaces.Service;

public class People implements Service {

    private final Repository repository;

    @Inject
    public People(Repository repository) {

        this.repository = repository;
    }

    @Override
    public Map<String, Object> getPersonById(Integer id) {

        return toLinked(repository.getById(id));
    }

    @Override
    public Collection<Map<String, Object>> getAllPeople() {

        return repository.getAll().stream().map(this::toLinked).collect(Collectors.toList());
    }

    private Map<String, Object> toLinked(Person person) {

        return new HashMap<String, Object>() {
            {
                put("person", person);
                put("link", "/person/" + person.getId());
            }
        };
    }
}
