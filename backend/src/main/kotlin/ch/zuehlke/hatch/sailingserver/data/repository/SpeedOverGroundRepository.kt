package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventStore
import ch.zuehlke.hatch.sailingserver.domain.SpeedOverGroundMeasurement
import ch.zuehlke.hatch.sailingserver.livecache.LiveCache
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Repository
class SpeedOverGroundRepository (
        private val eventStore: EventStore,
        private val liveUpdateRepository: LiveUpdateRepository
) {

    private val liveCache: LiveCache<SpeedOverGroundMeasurement, TimeBasedIdentifier>

    init {
        val liveStream = this.liveUpdateRepository.getLiveStream(SpeedOverGroundTransformer())
        this.liveCache = LiveCache(liveStream) { position -> TimeBasedIdentifier(position.timestamp) }
    }

    fun getSpeedOverGround(): Flux<SpeedOverGroundMeasurement> {
        return this.liveUpdateRepository.getLiveStream(SpeedOverGroundTransformer())
    }

    fun getSpeedOverGround(from: LocalDateTime): Flux<SpeedOverGroundMeasurement> {
        val snapshotStream = this.eventStore.find(from, SpeedOverGroundTransformer())
        return this.liveCache.withSnapshot(snapshotStream)
    }

    fun getHistoricSpeedOverGround(from: LocalDateTime, to: LocalDateTime): Flux<SpeedOverGroundMeasurement> {
        return this.eventStore.find(from, to, SpeedOverGroundTransformer())
    }

}