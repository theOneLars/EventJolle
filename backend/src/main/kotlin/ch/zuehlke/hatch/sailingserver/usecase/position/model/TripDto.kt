package ch.zuehlke.hatch.sailingserver.usecase.position.model

import java.time.LocalDate

data class TripDto(
        val date: LocalDate,
        val id: String,
        val positions: List<PositionDto>
)