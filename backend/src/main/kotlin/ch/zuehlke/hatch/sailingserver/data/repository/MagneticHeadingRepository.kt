package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.StreamHealthProcessor
import ch.zuehlke.hatch.sailingserver.data.eventstore.EventStore
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindAngleMeasurement
import ch.zuehlke.hatch.sailingserver.domain.MagneticHeadingMeasurement
import ch.zuehlke.hatch.sailingserver.domain.MeasurementMessage
import ch.zuehlke.hatch.sailingserver.livecache.LiveCache
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Repository
class MagneticHeadingRepository(
        private val eventStore: EventStore,
        private val liveUpdateRepository: LiveUpdateRepository
        ) {

    private val liveCache: LiveCache<MagneticHeadingMeasurement, TimeBasedIdentifier>
    private val streamHealthProcessor = StreamHealthProcessor<MagneticHeadingMeasurement>()

    init {
        val liveStream = this.liveUpdateRepository.getLiveStream(MagneticHeadingTransformer())
        this.liveCache = LiveCache(liveStream) { heading -> TimeBasedIdentifier(heading.timestamp) }
    }

    fun getMagneticHeading(): Flux<MeasurementMessage<MagneticHeadingMeasurement>> {
        return streamHealthProcessor.process(this.liveUpdateRepository.getLiveStream(MagneticHeadingTransformer()))
    }

    fun getMagneticHeading(from: LocalDateTime): Flux<MeasurementMessage<MagneticHeadingMeasurement>> {
        val find = this.eventStore.find(from, MagneticHeadingTransformer())
        return streamHealthProcessor.process(this.liveCache.withSnapshot(find))
    }

    fun getHistoryMagneticHeading(from: LocalDateTime, to: LocalDateTime): Flux<MeasurementMessage<MagneticHeadingMeasurement>> {
        return streamHealthProcessor.process(this.eventStore.find(from, to, MagneticHeadingTransformer()))
    }
}