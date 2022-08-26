## OJUG Meetup.com Export

Exporting our Events from Meetup.com and into our ojug.org website

### Export from Meetup

Exported using the Meetup GraphQL API, and their Playground page https://www.meetup.com/api/playground/#graphQl-playground

Query used was:
```graphql
  query($eventId: String!) {
    groupByUrlname(urlname: $eventId) {
      description
      pastEvents(input: {first: 100}) {
        count
        pageInfo {
          endCursor
          hasNextPage
        }
        edges {
          node {
            title
            description
            dateTime
            going
          }
        }
      }
    }
  }
```
With inputs:
```json
  {"eventId":"omahajava"}
```

This generated the [meetup-events-export-2022-08-25.json](src/meetup-events-export-2022-08-25.json) file

### Import to ojug.org

Not really an import, but a generator to create markdown files from the JSON, to be used in https://github.com/OJUG/ojug.github.io/tree/master/_posts

[PostGenerator.groovy](src/PostGenerator.groovy) does the markdown generation

[PostGeneratorSpec.groovy](test/PostGeneratorSpec.groovy) has some tests, but also the generation kickoff (Ignored by default)
