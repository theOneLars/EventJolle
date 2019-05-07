package ch.zuehlke.hatch.sailingserver.data.receiver

import ch.zuehlke.hatch.sailingserver.data.CollectionNames
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalkSubscription
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SubscriptionInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.reactivestreams.client.MongoDatabase
import com.mongodb.reactivestreams.client.Success
import org.bson.Document
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Mono
import java.net.URI
import javax.annotation.PostConstruct

@Component
class MessageReceiver(private val database: MongoDatabase) {

    @Value("\${signalk.subscription.endpoint}")
    lateinit var signalkSubscriptionEndpoint: String

    private val initialSubscription = SignalkSubscription(
            "vessels.self",
            listOf(SubscriptionInfo("*", "1000", "delta", "instant", "1000")))

    private val objectMapper = ObjectMapper()
    private val collectionNames = CollectionNames();

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
        val path = this.getPath(document);
        val collectionName = this.collectionNames.getByPath(path)

        return this.database
                .getCollection(collectionName)
                .insertOne(document)
    }

    private fun getPath(document: Document): String {
        val propertyAccessor = DocumentPropertyAccessor(document)

        if (propertyAccessor.containsPath("updates[0].values[0].path")) {
            return propertyAccessor.getString("updates[0].values[0].path")
        } else {
            return "";
        }
    }

    private fun mapToDocument(content: String): Document {
        val sanitizedContent = content.replace("\$source", "_source")

        return Document.parse(sanitizedContent)
    }
}