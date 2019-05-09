package ch.zuehlke.hatch.sailingserver.data.eventstore

import org.bson.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DocumentValueExtractor(private val values: List<Pair<LocalDateTime, Document>> ) {

    fun <T> extract(extractor: (time: LocalDateTime, document: Document) -> T): List<T> {
        return values.map { extractor(it.first, it.second) }
    }

    companion object {
        fun from(event: Document, path: String): DocumentValueExtractor {
            if(event.containsKey("updates")) {
                val updates = event["updates"] as List<Document>
                val values = updates.flatMap { update ->
                    val timestamp = update.getString("timestamp")
                    val datetime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME)
                    val values = update["values"] as List<Document>
                    values
                            .filter { it.getString("path") == path }
                            .map { Pair(datetime, it) }
                }
                return DocumentValueExtractor(values)
            }
            return DocumentValueExtractor(emptyList())
        }
    }

}
