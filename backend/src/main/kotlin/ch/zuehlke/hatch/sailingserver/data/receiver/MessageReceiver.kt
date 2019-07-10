package ch.zuehlke.hatch.sailingserver.data.receiver

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventStore
import ch.zuehlke.hatch.sailingserver.data.eventstore.Success
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalkSubscription
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SubscriptionInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Mono
import java.net.URI
import javax.annotation.PostConstruct

@Component
class MessageReceiver(private val eventStore: EventStore) {

    @Value("\${signalk.subscription.endpoint}")
    lateinit var signalkSubscriptionEndpoint: String

    private val initialSubscription = SignalkSubscription(
            "vessels.self",
            listOf(SubscriptionInfo("*", "1000", "delta", "instant", "10")))

    private val objectMapper = ObjectMapper()
    private val parser = JsonParser()

    @PostConstruct
    fun startReceiver() {
        val client = ReactorNettyWebSocketClient()
        client.execute(
                URI.create(signalkSubscriptionEndpoint)
        ) { session ->
            session
                    .send(Mono.just(session.textMessage(objectMapper.writeValueAsString(initialSubscription))))
                    .thenMany(session.receive()
                            .map { it.payloadAsText }
                            .map { this.parseJsonObject(it) }
                            .flatMap { this.store(it) }
                    )
                    .then()
        }.subscribe()
    }

    private fun store(payload: JsonObject): Publisher<Success> {
        return this. eventStore.insert(payload)
    }

    private fun parseJsonObject(payload: String): JsonObject {
        val sanitizedContent = payload.replace("\$source", "_source")

        return this.parser.parse(sanitizedContent).asJsonObject
    }
}