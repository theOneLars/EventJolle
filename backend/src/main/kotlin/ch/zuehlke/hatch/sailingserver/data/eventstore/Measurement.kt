package ch.zuehlke.hatch.sailingserver.data.eventstore

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class Measurement(
        @Id
        private val id: Long?,

        val timestamp: LocalDateTime,

        val path: String,

        val measurement: String
)