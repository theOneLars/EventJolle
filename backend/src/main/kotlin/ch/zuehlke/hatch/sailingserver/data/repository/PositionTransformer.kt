package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.DocumentValueExtractor
import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.domain.Position
import org.bson.Document

class PositionTransformer : EventTransformer<Position> {

    override fun transform(document: Document): List<Position> {
        val extractor = DocumentValueExtractor.from(document, getPath())
        return extractor.extract { timestamp, document ->
            val valueDocument = document["value"] as Document
            val longitude = valueDocument.getDouble("longitude")
            val latitude = valueDocument.getDouble("latitude")
            Position(timestamp, longitude, latitude)
        }
    }

    override fun getPath(): String {
        return "navigation.position"
    }

}
