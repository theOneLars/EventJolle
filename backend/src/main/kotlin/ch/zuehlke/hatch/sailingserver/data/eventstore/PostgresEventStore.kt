package ch.zuehlke.hatch.sailingserver.data.eventstore

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Component
class PostgresEventStore(
        val repository: MeasurementRepository
) : EventStore {

    private val logger = LoggerFactory.getLogger(PostgresEventStore::class.java)
    private val parser = JsonParser()

    override fun <T> find(from: LocalDateTime, transformer: EventTransformer<T>): Flux<T> {
        return this.repository
                .findByPathAndTimestampIsGreaterThanEqual(transformer.getPath(), from)
                .map { parseJson(it.measurement) }
                .flatMap { document -> Flux.fromIterable(transformer.transform(document)) }
    }

    override fun <T> find(from: LocalDateTime, to: LocalDateTime, transformer: EventTransformer<T>): Flux<T> {
        return this.repository
                .findByPathAndTimestampIsBetween(transformer.getPath(), from, to)
                .map { parseJson(it.measurement) }
                .flatMap { document -> Flux.fromIterable(transformer.transform(document)) }
    }

    override fun insert(document: JsonObject): Publisher<Success> {
        val measurements = JsonToMeasurementConverter.extractFrom(document)

        logger.debug("Insert measurements {} in storage.", measurements)

        return this.repository
                .saveAll(measurements)
                .map { Success.SUCCESS }
    }

    private fun parseJson(json: String): JsonObject {
        return this.parser.parse(json).asJsonObject
    }
}