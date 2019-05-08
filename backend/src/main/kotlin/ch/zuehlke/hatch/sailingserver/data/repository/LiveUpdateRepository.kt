package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalkSubscription
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SubscriptionInfo
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI

@Component
class LiveUpdateRepository {

    @Value("\${signalk.subscription.endpoint}")
    lateinit var signalkSubscriptionEndpoint: String

    private val initialSubscription = SignalkSubscription(
            "vessels.self",
            listOf(SubscriptionInfo("*", "1000", "delta", "instant", "1000")))
    private val objectMapper = ObjectMapper()

    private val publisher: ConnectableFlux<Document>

    init {
        val sourceFlux = Flux.create<Document> { fluxSink ->
            run {
                val client = ReactorNettyWebSocketClient()
                client.execute(
                        URI.create(signalkSubscriptionEndpoint)
                ) { session ->
                    val publish = session.receive()
                            .map { message -> message.payloadAsText }
                            .map { this.mapToDocument(it) }
                            .publish()
                    publish.subscribe { event ->
                        fluxSink.next(event)
                    }
                    publish.connect()

                    session
                            .send(Mono.just(session.textMessage(objectMapper.writeValueAsString(initialSubscription))))
                            .thenMany(publish)
                            .then()
                }.subscribe()
            }
        }

        this.publisher = sourceFlux.publish()
    }

    fun <T> getLiveStream(transformer: EventTransformer<T>): Flux<T> {
        return Flux.from(this.publisher)
                .flatMap { Flux.fromIterable(transformer.transform(it)) }
    }

    fun getLiveStream(collectionName: String): Flux<Document> {
        return Flux.from(this.publisher)
    }

    private fun mapToDocument(content: String): Document {
        val sanitizedContent = content.replace("\$source", "_source")

        return Document.parse(sanitizedContent)
    }
}
