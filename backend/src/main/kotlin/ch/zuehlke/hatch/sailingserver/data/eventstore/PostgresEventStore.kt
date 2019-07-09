package ch.zuehlke.hatch.sailingserver.data.eventstore

import org.bson.Document
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Component
@Primary
class PostgresEventStore(
        val repository: MeasurementRepository
) : EventStore {

    private val logger = LoggerFactory.getLogger(PostgresEventStore::class.java)

    override fun <T> find(from: LocalDateTime, transformer: EventTransformer<T>): Flux<T> {
        return this.repository
                .findByPathAndTimestampIsGreaterThanEqual(transformer.getPath(), from)
                .map { Document.parse(it.measurement) }
                .flatMap { document -> Flux.fromIterable(transformer.transform(document)) }
    }

    override fun <T> find(from: LocalDateTime, to: LocalDateTime, transformer: EventTransformer<T>): Flux<T> {
        return this.repository
                .findByPathAndTimestampIsBetween(transformer.getPath(), from, to)
                .map { Document.parse(it.measurement) }
                .flatMap { document -> Flux.fromIterable(transformer.transform(document)) }
    }

    override fun insert(document: Document): Publisher<Success> {
        val measurements = DocumentToMeasurementConverter.extractFrom(document)

        logger.debug("Insert measurements {} in storage.", measurements)

        return this.repository
                .saveAll(measurements)
                .map { Success.SUCCESS }
    }
}