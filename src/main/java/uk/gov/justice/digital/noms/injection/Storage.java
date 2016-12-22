package uk.gov.justice.digital.noms.injection;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import uk.gov.justice.digital.noms.data.Person;
import uk.gov.justice.digital.noms.interfaces.Repository;

public class Storage implements Repository {

    private Map<Integer, Person> people = new HashMap<>();

    public Storage() {

        addPerson(1, "John Smith", 23);
        addPerson(2, "Jane Doe", 35);
        addPerson(3, "Jack Jones", 41);
    }

    @Override
    public Person getById(Integer id) {

        return people.get(id);
    }

    @Override
    public Collection<Person> getAll() {

        return people.values();
    }

    private void addPerson(Integer id, String name, Integer age) {

        people.put(id, new Person(id, name, age));
    }
}
