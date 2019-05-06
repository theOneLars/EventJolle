package ch.zuehlke.hatch.sailingserver.signalk;

import ch.zuehlke.hatch.sailingserver.frontend.WebSocketController;
import ch.zuehlke.hatch.sailingserver.signalk.model.info.ServerInfo;
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalKSubscription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class SignalKService {

    private static final Logger log = LoggerFactory.getLogger(SignalKService.class);

    private WebsocketClientEndpoint clientEndPoint;
    private WebSocketController webSocketController;
    private String signalkSubscriptionEndpoint;
    private String signalkRestEndpoint;

    @Autowired
    public SignalKService(WebSocketController webSocketController,
        @Value("${signalk.subscription.endpoint}") String signalkSubscriptionEndpoint,
        @Value("${signalk.rest.endpoint}") String signalkRestEndpoint) {
        this.webSocketController = webSocketController;
        this.signalkSubscriptionEndpoint = signalkSubscriptionEndpoint;
        this.signalkRestEndpoint = signalkRestEndpoint;
    }

    public Object getFullServerInfo() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(signalkRestEndpoint, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            ServerInfo serverInfo = mapper.readValue(response.getBody(), ServerInfo.class);
            log.debug("Fetched Info from Server");
            return serverInfo;
        } catch (IOException e) {
            log.error("Could not read ServerInfo from SignalK Server", e);
        }

        throw new IllegalStateException("Could not connect to signalK server");
    }

    public void startWebsocketConection() {
        try {
            clientEndPoint = new WebsocketClientEndpoint(new URI(signalkSubscriptionEndpoint));
            clientEndPoint.addMessageHandler(message -> {
                log.info("Message in SignalKService: " + message);
                webSocketController.publish(message);
            });
        } catch (URISyntaxException e) {
            log.error("Could not start websocket connection", e);
        }
    }

    /**
     * Use this method to subscribe on signalK server. The subscription String can
     * be like "navigation*".
     */
    public void subscribeToSignalKServer(SignalKSubscription subscription) {
        clientEndPoint.sendMessage(subscription.toString());
    }
}
