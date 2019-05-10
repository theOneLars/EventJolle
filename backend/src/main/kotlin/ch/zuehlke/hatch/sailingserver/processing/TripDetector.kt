package ch.zuehlke.hatch.sailingserver.processing

import ch.zuehlke.hatch.sailingserver.domain.PositionMeasurement
import java.time.format.DateTimeFormatter

class TripDetector {

    fun getIdentifierFor(position: PositionMeasurement): String {
        return "Trip " + position.timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}