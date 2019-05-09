package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class ApparentWindSpeedMeasurement(val speed: Double, override val timestamp: LocalDateTime) : Measurement()