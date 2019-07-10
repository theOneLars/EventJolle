package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.data.eventstore.JsonValueExtractor
import ch.zuehlke.hatch.sailingserver.domain.MagneticHeadingMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import com.google.gson.JsonObject

class MagneticHeadingTransformer : EventTransformer<MagneticHeadingMeasurement> {

    override fun getPath(): String {
       return "navigation.headingMagnetic"
    }

    override fun transform(json: JsonObject): List<MagneticHeadingMeasurement> {
        val extractor = JsonValueExtractor.from(json, getPath())
        return extractor.extract { timestamp, valueJson ->
            val value = valueJson.getAsJsonPrimitive("value")
            val heading = Radiant(value.asDouble)
            MagneticHeadingMeasurement(heading, timestamp)
        }
    }
}