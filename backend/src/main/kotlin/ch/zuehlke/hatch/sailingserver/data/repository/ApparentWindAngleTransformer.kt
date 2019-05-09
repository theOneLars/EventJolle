package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.DocumentValueExtractor
import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindAngleMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import org.bson.Document

class ApparentWindAngleTransformer : EventTransformer<ApparentWindAngleMeasurement> {

    override fun getPath(): String {
        return "environment.wind.angleApparent"
    }

    override fun transform(document: Document): List<ApparentWindAngleMeasurement> {
        val extractor = DocumentValueExtractor.from(document, getPath())
        return extractor.extract { timestamp, valueDocument ->
            val radiant = Radiant(valueDocument.getDouble("value"))
            ApparentWindAngleMeasurement(radiant, timestamp)
        }
    }
}