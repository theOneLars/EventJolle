package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class SpeedOverGroundMeasurement(val speed: Double, val timestamp: LocalDateTime)