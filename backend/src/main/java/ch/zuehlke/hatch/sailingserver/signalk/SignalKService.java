package ch.zuehlke.hatch.sailingserver.signalk;

import ch.zuehlke.hatch.sailingserver.frontend.WebSocketController;
import ch.zuehlke.hatch.sailingserver.signalk.model.info.ServerInfo;
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalKSubscription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class SignalKService {

    private static final Logger log = LoggerFactory.getLogger(SignalKService.class);

    private final String SIGNALK_HOST = "localhost";
    private final String SIGNALK_PORT = "3000";

    private WebsocketClientEndpoint clientEndPoint;
    private WebSocketController webSocketController;

    @Autowired
    public SignalKService(WebSocketController webSocketController) {
        this.webSocketController = webSocketController;
    }

    public Object getFullServerInfo() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://" + SIGNALK_HOST + ":" + SIGNALK_PORT + "/signalk/", String.class);

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
            clientEndPoint = new WebsocketClientEndpoint(getSignalKServerUri());
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

    private URI getSignalKServerUri() throws URISyntaxException {
        return new URI("ws://" + SIGNALK_HOST + ":" + SIGNALK_PORT + "/signalk/v1/stream/?subscribe=none");
    }
}
