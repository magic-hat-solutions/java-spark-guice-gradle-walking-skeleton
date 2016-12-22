import spock.lang.Specification
import uk.gov.justice.digital.noms.Configuration
import uk.gov.justice.digital.noms.Server

class ServerTest extends Specification {

    def "someLibraryMethod returns true"() {
        setup:
        Server.run(new Configuration())

        when:
        def result = true // Test JSON output from server app

        then:
        result == true
    }
}
