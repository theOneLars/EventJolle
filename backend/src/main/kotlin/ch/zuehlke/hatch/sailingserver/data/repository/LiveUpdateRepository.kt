package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.CollectionNames
import ch.zuehlke.hatch.sailingserver.data.receiver.DocumentPropertyAccessor
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalkSubscription
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SubscriptionInfo
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.GroupedFlux
import reactor.core.publisher.Mono
import java.net.URI
import java.util.*
import java.util.function.Consumer
import javax.annotation.PostConstruct

@Component
class LiveUpdateRepository {

    @Value("\${signalk.subscription.endpoint}")
    lateinit var signalkSubscriptionEndpoint: String

    private val initialSubscription = SignalkSubscription(
            "vessels.self",
            listOf(SubscriptionInfo("*", "1000", "delta", "instant", "1000")))
    private val objectMapper = ObjectMapper()
    private val collectionNames = CollectionNames();

    private val publisher: ConnectableFlux<GroupedFlux<String, Document>>

    init {
        val sourceFlux = Flux.create<GroupedFlux<String, Document>> { fluxSink ->
            run {
                val client = ReactorNettyWebSocketClient()
                client.execute(
                        URI.create(signalkSubscriptionEndpoint)
                ) { session ->
                    val publish = session.receive()
                            .map { message -> message.payloadAsText }
                            .map { this.mapToDocument(it) }
                            .groupBy { this.groupByCollectionName(it) }
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

    fun getLiveStream(collectionName: String): Flux<Document> {
        return this.publisher
                .autoConnect()
                .filter { groupedFlux -> Objects.equals(groupedFlux.key(), collectionName) }
                .flatMap { it }
    }

    private fun groupByCollectionName(document: Document): String {
        val path = this.getPath(document)
        return this.collectionNames.getByPath(path)
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
