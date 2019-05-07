package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class MagneticHeadingMeasurement(val heading: Heading, val timestamp: LocalDateTime)