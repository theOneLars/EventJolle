package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.PositionTransformer
import ch.zuehlke.hatch.sailingserver.domain.Position
import ch.zuehlke.hatch.sailingserver.livecache.LiveCache
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import javax.annotation.PostConstruct

@Repository
class LivePositionRepository(
        private val positionRepository: PositionRepository,
        private val liveUpdateRepository: LiveUpdateRepository
) {

    private lateinit var liveCache: LiveCache<Position, TimeBasedIdentifier>

    @PostConstruct
    fun initialize() {
        val liveStream = this.liveUpdateRepository.getLiveStream(PositionTransformer())
                .log("live stream")
        this.liveCache = LiveCache(liveStream) { position -> TimeBasedIdentifier(position.timestamp) }
    }

    fun getPositions(): Flux<Position> {
        return this.liveUpdateRepository.getLiveStream(PositionTransformer())
                .log("live positions stream")
    }

    fun getPositions(from: LocalDateTime): Flux<Position> {
        val snapshotStream = this.positionRepository.getPositions(from)

        return this.liveCache.withSnapshot(snapshotStream)
    }

}