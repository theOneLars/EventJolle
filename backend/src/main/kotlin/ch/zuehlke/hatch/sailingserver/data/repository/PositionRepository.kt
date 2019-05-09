package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventStore
import ch.zuehlke.hatch.sailingserver.domain.PositionMeasurement
import ch.zuehlke.hatch.sailingserver.livecache.LiveCache
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Repository
class PositionRepository(
        private val eventStore: EventStore,
        private val liveUpdateRepository: LiveUpdateRepository
) {

    private val liveCache: LiveCache<PositionMeasurement, TimeBasedIdentifier>

    init {
        val liveStream = this.liveUpdateRepository.getLiveStream(PositionTransformer())
                .log("live stream")
        this.liveCache = LiveCache(liveStream) { position -> TimeBasedIdentifier(position.timestamp) }
    }

    fun getPositions(): Flux<PositionMeasurement> {
        return this.liveUpdateRepository.getLiveStream(PositionTransformer())
                .log("live positions stream")
    }

    fun getPositions(from: LocalDateTime): Flux<PositionMeasurement> {
        val snapshotStream = this.eventStore.find(from, PositionTransformer())

        return this.liveCache.withSnapshot(snapshotStream)
    }

    fun getHistoricPositions(from: LocalDateTime, to: LocalDateTime): Flux<PositionMeasurement> {
        return this.eventStore.find(from, to, PositionTransformer())
    }
}