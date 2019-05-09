package ch.zuehlke.hatch.sailingserver.processing

import ch.zuehlke.hatch.sailingserver.domain.PositionMeasurement
import java.time.Duration
import java.time.LocalDateTime

class PositionFilter(
        private val filterInterval: Duration
) {

    internal var lastTimestamp: LocalDateTime? = null

    fun accept(position: PositionMeasurement): Boolean {
        val currentTimestamp = position.timestamp

        if (this.lastTimestamp == null) {
            this.lastTimestamp = currentTimestamp
            return true
        } else if (currentTimestamp.minus(filterInterval).isAfter(lastTimestamp)) {
            this.lastTimestamp = currentTimestamp
            return true
        } else {
            return false
        }
    }
}