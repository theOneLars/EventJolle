package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class MagneticHeadingMeasurement(val heading: Radiant, val timestamp: LocalDateTime)