package ch.zuehlke.hatch.sailingserver.usecase.position.model

import java.time.LocalDateTime

data class PositionDto(
        val timestamp: LocalDateTime,
        val longitude: Double,
        val latitude: Double
)