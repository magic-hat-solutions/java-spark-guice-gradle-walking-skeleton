/*
 * This Spock specification was auto generated by running the Gradle 'init' task
 * by 'nick' at '12/22/16 9:01 AM' with Gradle 3.2.1
 *
 * @author nick, @date 12/22/16 9:01 AM
 */

import spock.lang.Specification

class LibraryTest extends Specification{
    def "someLibraryMethod returns true"() {
        setup:
        Library lib = new Library()
        when:
        def result = lib.someLibraryMethod()
        then:
        result == true
    }
}
