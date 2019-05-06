package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.config.ApplicationConfig.Companion.COLLECTION_NAME_EVENTS
import ch.zuehlke.hatch.sailingserver.domain.Position
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Repository
class PositionRepository @Autowired
constructor(private val database: MongoDatabase) {

    fun getPositions(from: LocalDateTime, to: LocalDateTime): Flux<Position> {
        return Flux.from(
                this.database.getCollection(COLLECTION_NAME_EVENTS)
                        .find()
        ).map { this.toPosition(it) }
    }

    private fun toPosition(document: Document): Position? {

        return null
    }
}