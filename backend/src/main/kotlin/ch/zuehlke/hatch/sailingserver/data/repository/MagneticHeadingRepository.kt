package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventStore
import ch.zuehlke.hatch.sailingserver.domain.MagneticHeadingMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Position
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import ch.zuehlke.hatch.sailingserver.livecache.LiveCache
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.annotation.PostConstruct

@Repository
class MagneticHeadingRepository(
        private val eventStore: EventStore,
        private val liveUpdateRepository: LiveUpdateRepository
        ) {

    private val liveCache: LiveCache<MagneticHeadingMeasurement, TimeBasedIdentifier>

    init {
        val liveStream = this.liveUpdateRepository.getLiveStream(MagneticHeadingTransformer())
        this.liveCache = LiveCache(liveStream) { heading -> TimeBasedIdentifier(heading.timestamp) }
    }

    fun getMagneticHeading(): Flux<MagneticHeadingMeasurement> {
        return this.liveUpdateRepository.getLiveStream(MagneticHeadingTransformer())
    }

    fun getMagneticHeading(from: LocalDateTime): Flux<MagneticHeadingMeasurement> {
        val find = this.eventStore.find(from, MagneticHeadingTransformer())
        return this.liveCache.withSnapshot(find)
    }

    fun getHistoryMagneticHeading(from: LocalDateTime, to: LocalDateTime): Flux<MagneticHeadingMeasurement> {
        return this.eventStore.find(from, to, MagneticHeadingTransformer())
    }
}