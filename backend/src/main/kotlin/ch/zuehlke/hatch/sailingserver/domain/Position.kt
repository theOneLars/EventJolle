package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class Position(val timestamp: LocalDateTime, val longitude: String, val latitude: String)