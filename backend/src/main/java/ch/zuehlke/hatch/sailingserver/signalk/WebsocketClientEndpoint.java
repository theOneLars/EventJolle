package ch.zuehlke.hatch.sailingserver.signalk;

import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalKSubscibtionFactory;
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalKSubscriptionPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;


@Component
public class WebsocketClientEndpoint {

    private final String SIGNALK_HOST = "localhost";
    private final String SIGNALK_PORT = "3000";

    private static final Logger log = LoggerFactory.getLogger(WebsocketClientEndpoint.class);

    public void connect() {
        WebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(
                URI.create("ws://" + SIGNALK_HOST + ":" + SIGNALK_PORT + "/signalk/v1/stream/?subscribe=none"),
                session ->
                        session
                                .send(Mono.just(session.textMessage(getInitialSubscription())))
                                .thenMany(session.receive()
                                        .map(WebSocketMessage::getPayloadAsText)
                                        .log())
                                .then())
                .subscribe();

    }

    private String getInitialSubscription() {
        return SignalKSubscibtionFactory.createDefaultSubscriptionWithSinglePath(
                    "*",
                    1000,
                    SignalKSubscriptionPath.FORMAT_DELTA,
                    SignalKSubscriptionPath.POLICY_INSTANT,
                    1000).toString();
    }

}
