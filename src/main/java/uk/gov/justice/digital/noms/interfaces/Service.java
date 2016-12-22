package uk.gov.justice.digital.noms.interfaces;

import java.util.Map;
import java.util.Collection;

public interface Service {

    Map<String, Object> getPersonById(Integer id);

    Collection<Map<String, Object>> getAllPeople();
}
