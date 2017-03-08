package uk.gov.justice.digital.noms.interfaces;

import java.util.Map;

public interface Service {

    Map<String, Object> findPerson(String forename, String surname);
}
