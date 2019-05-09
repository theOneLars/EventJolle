package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

abstract class Measurement {
    abstract val timestamp: LocalDateTime
}
