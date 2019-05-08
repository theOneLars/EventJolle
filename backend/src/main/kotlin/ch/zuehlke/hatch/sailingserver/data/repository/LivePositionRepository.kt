package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.CollectionNames.Companion.COLLECTION_NAME_EVENTS_NAVIGATION_POSITION
import ch.zuehlke.hatch.sailingserver.data.receiver.DocumentPropertyAccessor
import ch.zuehlke.hatch.sailingserver.domain.Position
import ch.zuehlke.hatch.sailingserver.livecache.LiveCache
import org.bson.Document
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
        val liveStream = this.liveUpdateRepository.getLiveStream(COLLECTION_NAME_EVENTS_NAVIGATION_POSITION)
                .map { this.toPosition(it) }
                .log("live stream")
        this.liveCache = LiveCache(liveStream) { position -> TimeBasedIdentifier(position.timestamp) }
    }

    fun getPositions(): Flux<Position> {
        return this.liveUpdateRepository.getLiveStream(COLLECTION_NAME_EVENTS_NAVIGATION_POSITION)
                .map { this.toPosition(it) }
    }

    fun getPositions(from: LocalDateTime): Flux<Position> {
        val snapshotStream = this.positionRepository.getPositions(from)

        return this.liveCache.withSnapshot(snapshotStream.log("before live cache")).log("after live cache")
    }

    private fun toPosition(document: Document): Position {
        val propertyAccessor = DocumentPropertyAccessor(document)
        return Position(
                timestamp = propertyAccessor.getTimestamp("updates[0].timestamp"),
                longitude = propertyAccessor.getDouble("updates[0].values[0].value.longitude"),
                latitude = propertyAccessor.getDouble("updates[0].values[0].value.latitude"))
    }
}