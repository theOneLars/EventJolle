package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.data.eventstore.JsonValueExtractor
import ch.zuehlke.hatch.sailingserver.domain.CourseOverGroundMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import com.google.gson.JsonObject

class CourseOverGroundTransformer : EventTransformer<CourseOverGroundMeasurement>{

    override fun getPath(): String {
        return "navigation.courseOverGroundTrue"
    }

    override fun transform(json: JsonObject): List<CourseOverGroundMeasurement> {
        val extractor = JsonValueExtractor.from(json, getPath())
        return extractor.extract { timestamp, valueJson ->
            val value = valueJson.getAsJsonPrimitive("value")
            val course = Radiant(value.asDouble)
            CourseOverGroundMeasurement(course, timestamp)
        }
    }
}
