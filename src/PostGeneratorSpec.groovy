import spock.lang.Ignore
import spock.lang.Specification

@Grab('org.spockframework:spock-core:2.3-groovy-4.0')
@GrabExclude('org.codehaus.groovy:*')
class PostGeneratorSpec extends Specification {
    def "makeFilename"(String title, String expected) {
        given:
        PostGenerator postGenerator = new PostGenerator()

        expect:
        postGenerator.makeFilename(title, '2014-05-20T17:30-05:00') == expected

        where:
        title || expected
        'lower' || '2014-05-20-lower.md'
        'UPPER' || '2014-05-20-upper.md'
        'easyCase' || '2014-05-20-easycase.md'
        'some123Nums' || '2014-05-20-some123nums.md'
        'I don\'t care' || '2014-05-20-i-dont-care.md'
        'some Really 123 Long title is here to stay' || '2014-05-20-some-really-123-long-title-is-here-.md'
    }

    def "convertToYYYMMDD"() {
        given:
        PostGenerator postGenerator = new PostGenerator()

        expect:
        postGenerator.convertToYYYMMDD('2014-05-20T17:30-05:00') == '2014-05-20'
    }

    def "convertToLongDate"() {
        given:
        PostGenerator postGenerator = new PostGenerator()

        expect:
        postGenerator.convertToLongDate('2014-05-20T17:30-05:00') == 'May 20, 2014'
        postGenerator.convertToLongDate('2020-10-05T17:30-05:00') == 'October 5, 2020'
    }

    /**
     * Running this will clear out the <projectroot>/output directory
     *  and generate all new posts from the json
     *
     * So it is ignored by default
     */
    @Ignore
    def "generatePosts"() {
        expect:
        new PostGenerator().generatePosts()
    }
}
