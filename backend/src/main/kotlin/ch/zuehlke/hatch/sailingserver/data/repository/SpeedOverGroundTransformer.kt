package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.data.eventstore.JsonValueExtractor
import ch.zuehlke.hatch.sailingserver.domain.SpeedOverGroundMeasurement
import com.google.gson.JsonObject

class SpeedOverGroundTransformer : EventTransformer<SpeedOverGroundMeasurement> {

    override fun getPath(): String {
        return "navigation.speedOverGround"
    }

    override fun transform(json: JsonObject): List<SpeedOverGroundMeasurement> {
        val extractor = JsonValueExtractor.from(json, getPath())
        return extractor.extract { timestamp, valueJson ->
            val value = valueJson.getAsJsonPrimitive("value")
            val speed = value.asDouble
            SpeedOverGroundMeasurement(speed, timestamp)
        }
    }
}
