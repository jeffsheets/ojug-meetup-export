import groovy.json.JsonSlurper
import groovy.text.SimpleTemplateEngine

import java.text.SimpleDateFormat

class PostGenerator {
    static final DEST_FOLDER = 'output'
    static final SRC_JSON = '/meetup-events-export-2025-01-11.json'

    String convertToYYYMMDD(String utcDate) {
        def inDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        def outDate = new SimpleDateFormat('yyyy-MM-dd')
        outDate.format(inDate.parse(utcDate))
    }

    String convertToLongDate(String utcDate) {
        def inDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        def outDate = new SimpleDateFormat('MMMMM d, yyyy')
        outDate.format(inDate.parse(utcDate))
    }

    String makeFilename(String title, String utcDate) {
        def titleNoSpaces = title.replaceAll(' ', '-')
        def titleClean = titleNoSpaces.replaceAll('[^a-zA-Z0-9\\-]+', '')
        def titleShort = titleClean.take(35)
        def dateShort = convertToYYYMMDD(utcDate)
        ("$dateShort-${titleShort}.md").toLowerCase()
    }

    void generatePosts() {
        def outDir = new File(DEST_FOLDER)
        if (outDir.exists()) {
            outDir.deleteDir()
        }
        outDir.mkdirs()

        def events = new JsonSlurper().parse(getClass().getResource(SRC_JSON)).data.groupByUrlname.pastEvents.edges
        events.each {
            def event = it.node
            def filename = makeFilename(event.title, event.dateTime)
            def outfile = new File("$DEST_FOLDER/$filename")
            def filecontents = new SimpleTemplateEngine()
                    .createTemplate(getClass().getResource('/post.gsp'))
                    .make([
                            title      : event.title.replaceAll('"', '\"'),
                            description: event.description,
                            longDate   : convertToLongDate(event.dateTime),
                            attended   : event.going
                    ])
                    .toString()
            outfile.write filecontents
        }
    }
}
