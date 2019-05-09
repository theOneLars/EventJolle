package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.DocumentValueExtractor
import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.domain.PositionMeasurement
import org.bson.Document

class PositionTransformer : EventTransformer<PositionMeasurement> {

    override fun transform(document: Document): List<PositionMeasurement> {
        val extractor = DocumentValueExtractor.from(document, getPath())
        return extractor.extract { timestamp, document ->
            val valueDocument = document["value"] as Document
            val longitude = valueDocument.getDouble("longitude")
            val latitude = valueDocument.getDouble("latitude")
            PositionMeasurement(timestamp, longitude, latitude)
        }
    }

    override fun getPath(): String {
        return "navigation.position"
    }

}
