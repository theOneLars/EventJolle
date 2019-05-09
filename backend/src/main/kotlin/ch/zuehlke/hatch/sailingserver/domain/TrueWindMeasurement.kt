package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class TrueWindMeasurement(override val timestamp: LocalDateTime, val trueWind: TrueWind) : Measurement()
