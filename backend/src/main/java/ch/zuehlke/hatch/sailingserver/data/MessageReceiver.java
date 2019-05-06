package ch.zuehlke.hatch.sailingserver.data;

import ch.zuehlke.hatch.sailingserver.config.ApplicationConfig;
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalKSubscriptionFactory;
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalKSubscriptionPath;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;

@Component
public class MessageReceiver {

    private final String SIGNALK_HOST = "localhost";
    private final String SIGNALK_PORT = "3000";

    private final MongoDatabase database;

    @Autowired
    public MessageReceiver(MongoDatabase database) {
        this.database = database;
    }

    @PostConstruct
    public void startReceiver() {
        WebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(
                URI.create("ws://" + SIGNALK_HOST + ":" + SIGNALK_PORT + "/signalk/v1/stream/?subscribe=none"),
                session ->
                        session
                                .send(Mono.just(session.textMessage(getInitialSubscription())))
                                .thenMany(session.receive()
                                        .map(WebSocketMessage::getPayloadAsText)
//                                        .log()
                                        .map(this::mapToDocument)
                                        .flatMap(this::store)
                                        .log()
                                )
                                .then())
                .subscribe();
    }

    private Publisher<Success> store(Document document) {
        return this.database
                .getCollection(ApplicationConfig.COLLECTION_NAME_EVENTS)
                .insertOne(document);
    }

    private Document mapToDocument(String content) {
        String sanitizedContent = content.replace("$source", "_source");

        return Document.parse(sanitizedContent);
    }

    private String getInitialSubscription() {
        return SignalKSubscriptionFactory.createDefaultSubscriptionWithSinglePath(
                "*",
                1000,
                SignalKSubscriptionPath.FORMAT_DELTA,
                SignalKSubscriptionPath.POLICY_INSTANT,
                1000).toString();
    }
}
