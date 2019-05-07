package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.config.ApplicationConfig.Companion.COLLECTION_NAME_EVENTS
import ch.zuehlke.hatch.sailingserver.data.receiver.DocumentPropertyAccessor
import ch.zuehlke.hatch.sailingserver.domain.Position
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.Document
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Repository
class LivePositionRepository(private val positionRepository: PositionRepository,
                             private val database: MongoDatabase) {

    fun getPositions(from: LocalDateTime): Flux<Position> {
        val liveStream = Flux.from(this.database.getCollection(COLLECTION_NAME_EVENTS).watch())
                .map { this.toPosition(it.fullDocument) }

        return this.positionRepository.getPositions(from, LocalDateTime.MAX)
                .concatWith(liveStream)
    }

    private fun toPosition(document: Document): Position? {
        val propertyAccessor = DocumentPropertyAccessor(document)
        return Position(
                timestamp = propertyAccessor.getTimestamp("updates[0].values[0].value.timestamp"),
                longitude = propertyAccessor.getDouble("updates[0].values[0].value.longitude"),
                latitude = propertyAccessor.getDouble("updates[0].values[0].value.latitude"))
    }
}