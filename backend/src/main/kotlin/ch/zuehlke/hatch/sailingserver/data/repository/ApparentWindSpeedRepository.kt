package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.StreamHealthProcessor
import ch.zuehlke.hatch.sailingserver.data.eventstore.EventStore
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindAngleMeasurement
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindSpeedMeasurement
import ch.zuehlke.hatch.sailingserver.domain.MeasurementMessage
import ch.zuehlke.hatch.sailingserver.livecache.LiveCache
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Repository
class ApparentWindSpeedRepository(
        private val eventStore: EventStore,
        private val liveUpdateRepository: LiveUpdateRepository
) {

    private val liveCache: LiveCache<ApparentWindSpeedMeasurement, TimeBasedIdentifier>
    private val streamHealthProcessor = StreamHealthProcessor<ApparentWindSpeedMeasurement>()

    init {
        val liveStream = this.liveUpdateRepository.getLiveStream(ApparentWindSpeedTransformer())
        this.liveCache = LiveCache(liveStream) { apparentWindSpeed -> TimeBasedIdentifier(apparentWindSpeed.timestamp) }
    }

    fun getApparentWindSpeeds(): Flux<MeasurementMessage<ApparentWindSpeedMeasurement>> {
        return streamHealthProcessor.process(this.liveUpdateRepository.getLiveStream(ApparentWindSpeedTransformer()))
    }

    fun getApparentWindSpeeds(from: LocalDateTime): Flux<MeasurementMessage<ApparentWindSpeedMeasurement>>  {
        val snapshotStream = this.eventStore.find(from, ApparentWindSpeedTransformer())

        return streamHealthProcessor.process(this.liveCache.withSnapshot(snapshotStream))
    }

    fun getHistoricApparentWindSpeeds(from: LocalDateTime, to: LocalDateTime): Flux<MeasurementMessage<ApparentWindSpeedMeasurement>> {
        return streamHealthProcessor.process(this.eventStore.find(from, to, ApparentWindSpeedTransformer()))
    }
}