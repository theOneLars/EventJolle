package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class PositionMeasurement(val timestamp: LocalDateTime, val longitude: Double, val latitude: Double)