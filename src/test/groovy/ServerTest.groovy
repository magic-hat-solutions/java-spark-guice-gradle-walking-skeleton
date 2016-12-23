import groovyx.net.http.RESTClient
import spock.lang.Specification
import uk.gov.justice.digital.noms.Configuration
import uk.gov.justice.digital.noms.Server

class ServerTest extends Specification {

    def "server returns person"() {
        setup:
        Server.run(new Configuration())

        when:
        def result = new RESTClient("http://localhost:8080/").get(path: "person/2")

        then:
        result.status == 200
        result.data.person.name == "Jane Doe"
    }
}
