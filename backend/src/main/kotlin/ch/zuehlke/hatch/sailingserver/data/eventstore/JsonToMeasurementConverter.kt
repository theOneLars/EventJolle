package ch.zuehlke.hatch.sailingserver.data.eventstore

import com.google.gson.JsonObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object JsonToMeasurementConverter {

    fun extractFrom(event: JsonObject): List<Measurement> {
        if(event.has("updates")) {
            val updates = event.getAsJsonArray("updates")
            val values = updates.flatMap { update ->
                val updateObject = update.asJsonObject
                val timestamp = updateObject["timestamp"].asString
                val datetime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME)
                val values = updateObject["values"].asJsonArray

                values.map { value -> Measurement(null, datetime, value.asJsonObject["path"].asString, event.toString() ) }
            }

            return values
        }
        return emptyList()
    }
}
