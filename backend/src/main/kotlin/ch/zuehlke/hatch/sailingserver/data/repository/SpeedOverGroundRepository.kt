package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.StreamHealthProcessor
import ch.zuehlke.hatch.sailingserver.data.eventstore.EventStore
import ch.zuehlke.hatch.sailingserver.domain.MagneticHeadingMeasurement
import ch.zuehlke.hatch.sailingserver.domain.MeasurementMessage
import ch.zuehlke.hatch.sailingserver.domain.SpeedOverGroundMeasurement
import ch.zuehlke.hatch.sailingserver.livecache.LiveCache
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Repository
class SpeedOverGroundRepository(
        private val eventStore: EventStore,
        private val liveUpdateRepository: LiveUpdateRepository
) {

    private val liveCache: LiveCache<SpeedOverGroundMeasurement, TimeBasedIdentifier>
    private val streamHealthProcessor = StreamHealthProcessor<SpeedOverGroundMeasurement>()

    init {
        val liveStream = this.liveUpdateRepository.getLiveStream(SpeedOverGroundTransformer())
        this.liveCache = LiveCache(liveStream) { position -> TimeBasedIdentifier(position.timestamp) }
    }

    fun getSpeedOverGround(): Flux<MeasurementMessage<SpeedOverGroundMeasurement>> {
        return streamHealthProcessor.process(this.liveUpdateRepository.getLiveStream(SpeedOverGroundTransformer()))
    }

    fun getSpeedOverGround(from: LocalDateTime): Flux<MeasurementMessage<SpeedOverGroundMeasurement>> {
        val snapshotStream = this.eventStore.find(from, SpeedOverGroundTransformer())
        return streamHealthProcessor.process(this.liveCache.withSnapshot(snapshotStream))
    }

    fun getHistoricSpeedOverGround(from: LocalDateTime, to: LocalDateTime): Flux<MeasurementMessage<SpeedOverGroundMeasurement>> {
        return streamHealthProcessor.process(this.eventStore.find(from, to, SpeedOverGroundTransformer()))
    }

}