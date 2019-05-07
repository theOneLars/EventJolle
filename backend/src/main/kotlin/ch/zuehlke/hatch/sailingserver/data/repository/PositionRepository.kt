package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.CollectionNames.Companion.COLLECTION_NAME_EVENTS_NAVIGATION_POSITION
import ch.zuehlke.hatch.sailingserver.data.receiver.DocumentPropertyAccessor
import ch.zuehlke.hatch.sailingserver.domain.Position
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Sorts
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class PositionRepository @Autowired
constructor(private val database: MongoDatabase) {

    private val PROPERTY_TIMESTAMP = "updates.values.value.timestamp"

    fun getPositions(from: LocalDateTime, to: LocalDateTime): Flux<Position> {
        val filter = and(
                gte(PROPERTY_TIMESTAMP, from.format(DateTimeFormatter.ISO_DATE_TIME)),
                lt(PROPERTY_TIMESTAMP, to.format(DateTimeFormatter.ISO_DATE_TIME))
        )

        return Flux.from(
                this.database
                        .getCollection(COLLECTION_NAME_EVENTS_NAVIGATION_POSITION)
                        .find(filter)
                        .sort(Sorts.ascending(PROPERTY_TIMESTAMP))
        ).map { this.toPosition(it) }
    }

    private fun toPosition(document: Document): Position? {
        val propertyAccessor = DocumentPropertyAccessor(document)
        return Position(
                timestamp = propertyAccessor.getTimestamp("updates[0].values[0].value.timestamp"),
                longitude = propertyAccessor.getDouble("updates[0].values[0].value.longitude"),
                latitude = propertyAccessor.getDouble("updates[0].values[0].value.latitude"))
    }
}