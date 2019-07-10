package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.livecache.TemporalIdentifier
import java.time.LocalDateTime

data class TimeBasedIdentifier(val time: LocalDateTime) : TemporalIdentifier<TimeBasedIdentifier> {

    override fun isAfter(identifier: TimeBasedIdentifier): Boolean {
        return this.time.isAfter(identifier.time)
    }

}