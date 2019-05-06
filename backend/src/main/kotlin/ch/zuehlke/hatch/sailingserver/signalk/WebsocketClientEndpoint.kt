package ch.zuehlke.hatch.sailingserver.signalk

import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalkSubscription
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SubscriptionInfo
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Mono
import java.net.URI


@Component
class WebsocketClientEndpoint {

    @Value("\${signalk.subscription.endpoint}")
    lateinit var signalkSubscriptionEndpoint: String

    private val objectMapper = ObjectMapper()
    private val initialSubscription = SignalkSubscription(
            "vessels.self",
            listOf(SubscriptionInfo("*", "1000", "delta", "instant", "1000")))


    fun connect() {
        val client = ReactorNettyWebSocketClient()
        client.execute(
                URI.create(signalkSubscriptionEndpoint)
        ) { session ->
            session
                    .send(Mono.just(session.textMessage(objectMapper.writeValueAsString(initialSubscription))))
                    .thenMany(session.receive()
                            .map<String> { message -> message.payloadAsText }
                            .log())
                    .then()
        }
                .subscribe()

    }

}
