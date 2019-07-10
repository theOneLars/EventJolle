package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.data.eventstore.JsonValueExtractor
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindAngleMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import com.google.gson.JsonObject

class ApparentWindAngleTransformer : EventTransformer<ApparentWindAngleMeasurement> {

    override fun getPath(): String {
        return "environment.wind.angleApparent"
    }

    override fun transform(json: JsonObject): List<ApparentWindAngleMeasurement> {
        val extractor = JsonValueExtractor.from(json, getPath())
        return extractor.extract { timestamp, valueJson ->
            val value = valueJson.getAsJsonPrimitive("value")
            val radiant = Radiant(value.asDouble)
            ApparentWindAngleMeasurement(radiant, timestamp)
        }
    }
}