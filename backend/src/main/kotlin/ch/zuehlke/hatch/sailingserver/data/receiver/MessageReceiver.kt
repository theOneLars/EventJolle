package ch.zuehlke.hatch.sailingserver.data.receiver

import ch.zuehlke.hatch.sailingserver.config.ApplicationConfig
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalkSubscription
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SubscriptionInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.reactivestreams.client.MongoDatabase
import com.mongodb.reactivestreams.client.Success
import org.bson.Document
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Mono
import java.net.URI
import javax.annotation.PostConstruct

@Component
class MessageReceiver {

    @Value("\${signalk.subscription.endpoint}")
    lateinit var signalkSubscriptionEndpoint: String

    private val initialSubscription = SignalkSubscription(
            "vessels.self",
            listOf(SubscriptionInfo("*", "1000", "delta", "instant", "1000")))

    private val database: MongoDatabase
    private val objectMapper = ObjectMapper()


    @Autowired
    constructor(database: MongoDatabase) {
        this.database = database
    }

    @PostConstruct
    fun startReceiver() {
        val client = ReactorNettyWebSocketClient()
        client.execute(
                URI.create(signalkSubscriptionEndpoint)
        ) { session ->
            session
                    .send(Mono.just(session.textMessage(objectMapper.writeValueAsString(initialSubscription))))
                    .thenMany(session.receive()
                            .map { it.getPayloadAsText() }
                            .log()
                            .map { this.mapToDocument(it) }
                            .flatMap { this.store(it) }
                            .log()
                    )
                    .then()
        }.subscribe()
    }

    private fun store(document: Document): Publisher<Success> {
        return this.database
                .getCollection(ApplicationConfig.COLLECTION_NAME_EVENTS)
                .insertOne(document)
    }

    private fun mapToDocument(content: String): Document {
        val sanitizedContent = content.replace("\$source", "_source")

        return Document.parse(sanitizedContent)
    }
}