package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.DocumentValueExtractor
import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindSpeedMeasurement
import org.bson.Document

class ApparentWindSpeedTransformer : EventTransformer<ApparentWindSpeedMeasurement> {

    override fun getPath(): String {
        return "environment.wind.speedApparent"
    }

    override fun transform(document: Document): List<ApparentWindSpeedMeasurement> {
        val extractor = DocumentValueExtractor.from(document, getPath())
        return extractor.extract { timestamp, valueDocument ->
            val speed = valueDocument.getDouble("value")
            ApparentWindSpeedMeasurement(speed, timestamp)
        }
    }
}