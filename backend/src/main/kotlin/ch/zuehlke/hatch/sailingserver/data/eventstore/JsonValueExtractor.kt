package ch.zuehlke.hatch.sailingserver.data.eventstore

import com.google.gson.JsonObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JsonValueExtractor(private val values: List<Pair<LocalDateTime, JsonObject>> ) {

    fun <T> extract(extractor: (timestamp: LocalDateTime, valueJson: JsonObject) -> T): List<T> {
        return values.map { extractor(it.first, it.second) }
    }

    companion object {
        fun from(document: JsonObject, path: String): JsonValueExtractor {
            if(document.has("updates")) {
                val updates = document.getAsJsonArray("updates")

                val values = updates.flatMap { update ->
                    val updateObject = update.asJsonObject

                    val timestamp = updateObject["timestamp"].asString
                    val datetime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME)

                    updateObject.getAsJsonArray("values")
                            .filter { it.asJsonObject.get("path").asString == path }
                            .map { Pair(datetime, it.asJsonObject) }
                }
                return JsonValueExtractor(values)
            }
            return JsonValueExtractor(emptyList())
        }
    }
}
