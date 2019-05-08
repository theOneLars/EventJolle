package ch.zuehlke.hatch.sailingserver.data.eventstore

import ch.zuehlke.hatch.sailingserver.domain.Position
import org.bson.Document

class PositionTransformer : EventTransformer<Position> {

    override fun transform(document: Document): List<Position> {
        val extractor = DocumentValueExtractor.from(document, getPath())
        return extractor.extract { time, document ->
            val longitude = document.getDouble("longitude")
            val latitude = document.getDouble("latitude")
            Position(time, longitude, latitude)
        }
    }

    override fun getPath(): String {
        return "navigation.position"
    }

}
