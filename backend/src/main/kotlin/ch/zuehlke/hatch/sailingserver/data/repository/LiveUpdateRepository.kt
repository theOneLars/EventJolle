package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalkSubscription
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SubscriptionInfo
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.Document
import org.reactivestreams.Processor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.DirectProcessor
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI
import java.util.function.BiConsumer
import javax.annotation.PostConstruct

@Component
class LiveUpdateRepository {

    private val logger = LoggerFactory.getLogger(LiveUpdateRepository::class.java)

    @Value("\${signalk.subscription.endpoint}")
    lateinit var signalkSubscriptionEndpoint: String

    private val initialSubscription = SignalkSubscription(
            "vessels.self",
            listOf(SubscriptionInfo("*", "1000", "delta", "instant", "10")))
    private val objectMapper = ObjectMapper()

    private val processor = DirectProcessor.create<Document>()

    @PostConstruct
    fun initialize() {

        val client = ReactorNettyWebSocketClient()
        client.execute(
                URI.create(signalkSubscriptionEndpoint)
        ) { session ->
            val publish = session.receive()
                    .map { message -> message.payloadAsText }
                    .map { this.mapToDocument(it) }
                    .publish()
            publish.subscribe { event ->
                processor.onNext(event);
            }
            publish.connect()

            session
                    .send(Mono.just(session.textMessage(objectMapper.writeValueAsString(initialSubscription))))
                    .thenMany(publish)
                    .then()
        }.subscribe()
    }

    fun <T> getLiveStream(transformer: EventTransformer<T>): Flux<T> {
        return Flux.from(this.processor)
                .flatMap { Flux.fromIterable(transformer.transform(it)) }
                .onErrorContinue {
                    throwable, value -> logger.error("Error while transforming $value with $transformer.", throwable)
                }
    }

    private fun mapToDocument(content: String): Document {
        val sanitizedContent = content.replace("\$source", "_source")

        return Document.parse(sanitizedContent)
    }
}
