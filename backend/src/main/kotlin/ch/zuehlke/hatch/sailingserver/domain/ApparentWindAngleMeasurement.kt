package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class ApparentWindAngleMeasurement(val radiant: Radiant, override val timestamp: LocalDateTime) : Measurement()