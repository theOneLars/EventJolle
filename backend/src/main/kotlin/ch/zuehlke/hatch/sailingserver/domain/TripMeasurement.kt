package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class TripMeasurement(
        override val timestamp: LocalDateTime,
        val id: String,
        val positions: List<PositionMeasurement>
) : Measurement(), Comparable<TripMeasurement> {

    override fun compareTo(other: TripMeasurement): Int {
        return timestamp.compareTo(other.timestamp)
    }
}