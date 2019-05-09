package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.DocumentValueExtractor
import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.domain.CourseOverGroundMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import org.bson.Document

class CourseOverGroundTransformer : EventTransformer<CourseOverGroundMeasurement>{

    override fun transform(document: Document): List<CourseOverGroundMeasurement> {
        val extractor = DocumentValueExtractor.from(document, getPath())
        return extractor.extract { time, valueDocument ->
            val double = valueDocument.getDouble("value")
            CourseOverGroundMeasurement(Radiant(double), time)
        }
    }

    override fun getPath(): String {
        return "navigation.courseOverGroundTrue"
    }

}
