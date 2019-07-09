package ch.zuehlke.hatch.sailingserver.data.eventstore

import ch.zuehlke.hatch.sailingserver.data.repository.LiveUpdateRepository
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.Document
import org.bson.conversions.Bson
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class MongoDbEventStore(val database: MongoDatabase) : EventStore {

    private val logger = LoggerFactory.getLogger(LiveUpdateRepository::class.java)

    private val collectionName = "events"
    private val PROPERTY_TIMESTAMP = "updates.timestamp"
    private val PROPERTY_PATH = "updates.values.path"

    override fun <T> find(from: LocalDateTime, transformer: EventTransformer<T>) : Flux<T> {
        val filter = Filters.and(
                Filters.gte(PROPERTY_TIMESTAMP, from.format(DateTimeFormatter.ISO_DATE_TIME)),
                Filters.eq(PROPERTY_PATH, transformer.getPath())
        )
        return query(filter, transformer)
    }

    override fun <T> find(from: LocalDateTime, to: LocalDateTime, transformer: EventTransformer<T>) : Flux<T> {
        val filter = Filters.and(
                Filters.gte(PROPERTY_TIMESTAMP, from.format(DateTimeFormatter.ISO_DATE_TIME)),
                Filters.lt(PROPERTY_TIMESTAMP, to.format(DateTimeFormatter.ISO_DATE_TIME)),
                Filters.eq(PROPERTY_PATH, transformer.getPath())
        )
       return query(filter, transformer)
    }

    override fun insert(document: Document): Publisher<Success> {
        return this.database
                .getCollection(collectionName)
                .insertOne(document)
                .toFlux()
                .map { Success.SUCCESS }
    }

    private fun <T> query(filter: Bson, transformer: EventTransformer<T>): Flux<T> {
        val publisher = this.database
                .getCollection(collectionName)
                .find(filter)
                .sort(Sorts.ascending(PROPERTY_TIMESTAMP))
        return Flux.from(publisher)
                .flatMap { document -> Flux.fromIterable(transformer.transform(document)) }
                .onErrorContinue { throwable, value -> logger.error("Error while transforming $value with $transformer.", throwable) }
    }
}
