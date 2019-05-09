package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.DocumentValueExtractor
import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.domain.MagneticHeadingMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import org.bson.Document

class MagneticHeadingTransformer : EventTransformer<MagneticHeadingMeasurement> {

    override fun transform(document: Document): List<MagneticHeadingMeasurement> {
        val extractor = DocumentValueExtractor.from(document, getPath())
        return extractor.extract { time, valueDocument ->
            val double = valueDocument.getDouble("value")
            MagneticHeadingMeasurement(Radiant(double), time)
        }
    }

    override fun getPath(): String {
       return "navigation.courseOverGroundTrue"
    }

}