package ch.zuehlke.hatch.sailingserver.data.eventstore

import org.bson.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DocumentToMeasurementConverter {

    fun extractFrom(event: Document): List<Measurement> {
        if(event.containsKey("updates")) {
            val updates = event["updates"] as List<Document>
            val values = updates.flatMap { update ->
                val timestamp = update.getString("timestamp")
                val datetime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME)
                val values = update["values"] as List<Document>

                values.map { value -> Measurement(null, datetime, value.getString("path"), event.toJson()) }
            }

            return values
        }
        return emptyList()
    }
}
