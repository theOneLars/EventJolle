package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.data.eventstore.JsonValueExtractor
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindSpeedMeasurement
import com.google.gson.JsonObject

class ApparentWindSpeedTransformer : EventTransformer<ApparentWindSpeedMeasurement> {

    override fun getPath(): String {
        return "environment.wind.speedApparent"
    }

    override fun transform(json: JsonObject): List<ApparentWindSpeedMeasurement> {
        val extractor = JsonValueExtractor.from(json, getPath())
        return extractor.extract { timestamp, valueJson ->
            val value = valueJson.getAsJsonPrimitive("value")
            val speed = value.asDouble
            ApparentWindSpeedMeasurement(speed, timestamp)
        }
    }
}