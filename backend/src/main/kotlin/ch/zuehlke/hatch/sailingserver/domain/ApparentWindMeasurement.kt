package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class ApparentWindMeasurement(val wind: Wind, val timestamp: LocalDateTime)
