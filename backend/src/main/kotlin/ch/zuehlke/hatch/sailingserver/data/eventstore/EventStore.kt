package ch.zuehlke.hatch.sailingserver.data.eventstore

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.reactivestreams.client.MongoDatabase
import com.mongodb.reactivestreams.client.Success
import org.bson.Document
import org.bson.conversions.Bson
import org.reactivestreams.Publisher
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class EventStore(val database: MongoDatabase) {
    private val collectionName = "events"
    private val PROPERTY_TIMESTAMP = "updates.timestamp"
    private val PROPERTY_PATH = "updates.values.path"

    fun <T> find(from: LocalDateTime, transformer: EventTransformer<T>) : Flux<T> {
        val filter = Filters.and(
                Filters.gte(PROPERTY_TIMESTAMP, from.format(DateTimeFormatter.ISO_DATE_TIME)),
                Filters.eq(PROPERTY_PATH, transformer.getPath())
        )
        return query(filter, transformer)
    }

    fun <T> find(from: LocalDateTime, to: LocalDateTime, transformer: EventTransformer<T>) : Flux<T> {
        val filter = Filters.and(
                Filters.gte(PROPERTY_TIMESTAMP, from.format(DateTimeFormatter.ISO_DATE_TIME)),
                Filters.lt(PROPERTY_TIMESTAMP, to.format(DateTimeFormatter.ISO_DATE_TIME)),
                Filters.eq(PROPERTY_PATH, transformer.getPath())
        )
       return query(filter, transformer)
    }

    private fun <T> query(filter: Bson, transformer: EventTransformer<T>): Flux<T> {
        val publisher = this.database
                .getCollection(collectionName)
                .find(filter)
                .sort(Sorts.ascending(PROPERTY_TIMESTAMP))
        return Flux.from(publisher).flatMap { document ->
            Flux.fromIterable(transformer.transform(document))
        }
    }

    fun insert(document: Document): Publisher<Success> {
        return this.database.getCollection(collectionName).insertOne(document)
    }


}
