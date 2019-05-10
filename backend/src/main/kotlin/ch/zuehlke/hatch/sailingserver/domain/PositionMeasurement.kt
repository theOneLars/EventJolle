package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class PositionMeasurement(
        override val timestamp: LocalDateTime,
        val longitude: Double,
        val latitude: Double
) : Measurement() {

    fun getTimestampDate(): LocalDate {
        return timestamp.toLocalDate()
    }
}