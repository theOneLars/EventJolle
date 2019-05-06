package ch.zuehlke.hatch.sailingserver.signalk;

import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalKSubscriptionFactory;
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalKSubscriptionPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;


@Component
public class WebsocketClientEndpoint {

    @Value("${signalk.subscription.endpoint}")
    private String signalkSubscriptionEndpoint;

    public void connect() {
        WebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(
                URI.create(signalkSubscriptionEndpoint),
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
        return SignalKSubscriptionFactory.createDefaultSubscriptionWithSinglePath(
                    "*",
                    1000,
                    SignalKSubscriptionPath.FORMAT_DELTA,
                    SignalKSubscriptionPath.POLICY_INSTANT,
                    1000).toString();
    }

}
