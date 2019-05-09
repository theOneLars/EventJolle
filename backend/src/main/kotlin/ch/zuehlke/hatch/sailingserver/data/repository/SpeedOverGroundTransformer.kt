package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.DocumentValueExtractor
import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.domain.SpeedOverGroundMeasurement
import org.bson.Document

class SpeedOverGroundTransformer : EventTransformer<SpeedOverGroundMeasurement> {

    override fun transform(document: Document): List<SpeedOverGroundMeasurement> {
        val extractor = DocumentValueExtractor.from(document, getPath())
        return extractor.extract { time, document ->
            val double = document.getDouble("value")
            SpeedOverGroundMeasurement(double, time)
        }
    }

    override fun getPath(): String {
        return "navigation.speedOverGround"
    }

}
