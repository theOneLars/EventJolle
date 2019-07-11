package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.data.eventstore.JsonValueExtractor
import ch.zuehlke.hatch.sailingserver.domain.PositionMeasurement
import com.google.gson.JsonObject

class PositionTransformer : EventTransformer<PositionMeasurement> {

    override fun getPath(): String {
        return "navigation.position"
    }

    override fun transform(json: JsonObject): List<PositionMeasurement> {
        val extractor = JsonValueExtractor.from(json, getPath())
        return extractor.extract { timestamp, valueJson ->
            val value = valueJson.getAsJsonObject("value")
            val longitude: Double
            val latitude: Double
           if (value["longitude"].isJsonNull.not() && value["latitude"].isJsonNull.not()) {
                longitude = value["longitude"].asDouble
                latitude = value["latitude"].asDouble
            } else {
                longitude = 0.0
                latitude = 0.0
            }
            PositionMeasurement(timestamp, longitude, latitude)
        }
    }
}
