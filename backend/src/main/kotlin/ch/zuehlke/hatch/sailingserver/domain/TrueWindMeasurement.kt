package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class TrueWindMeasurement(val timestamp: LocalDateTime, val wind: Wind)
