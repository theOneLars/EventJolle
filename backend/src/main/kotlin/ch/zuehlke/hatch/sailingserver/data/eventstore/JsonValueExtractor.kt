package ch.zuehlke.hatch.sailingserver.data.eventstore

import com.google.gson.JsonObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JsonValueExtractor(private val values: List<Pair<LocalDateTime, JsonObject>> ) {

    fun <T> extract(extractor: (timestamp: LocalDateTime, valueJson: JsonObject) -> T): List<T> {
        return values.map { extractor(it.first, it.second) }
    }

    companion object {
        fun from(jsonObject: JsonObject, path: String): JsonValueExtractor {
            if(jsonObject.has("updates")) {
                val updates = jsonObject.getAsJsonArray("updates")

                val values = updates.flatMap { update ->
                    val updateObject = update.asJsonObject

                    if (updateObject.has("timestamp")) {
                        val timestamp = updateObject["timestamp"].asString
                        val datetime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME)

                        updateObject.getAsJsonArray("values")
                                .filter { it.asJsonObject.get("path").asString == path }
                                .map { Pair(datetime, it.asJsonObject) }
                    } else {
                        emptyList()
                    }
                }
                return JsonValueExtractor(values)
            }
            return JsonValueExtractor(emptyList())
        }
    }
}
