package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class VelocityMadeGoodMeasurement(override val timestamp: LocalDateTime, val velocityMadeGood: Double): Measurement()
