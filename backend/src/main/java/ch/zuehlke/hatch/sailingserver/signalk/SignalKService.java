package ch.zuehlke.hatch.sailingserver.signalk;

import ch.zuehlke.hatch.sailingserver.signalk.model.info.ServerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class SignalKService {

    private static final Logger log = LoggerFactory.getLogger(SignalKService.class);

    private WebsocketClientEndpoint websocketClientEndpoint;
    private String signalkRestEndpoint;


    @Autowired
    public SignalKService(WebsocketClientEndpoint websocketClientEndpoint, @Value("${signalk.rest.endpoint}")
        String signalkRestEndpoint) {
        this.websocketClientEndpoint = websocketClientEndpoint;
        this.signalkRestEndpoint = signalkRestEndpoint;
    }

    public Object getFullServerInfo() {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(signalkRestEndpoint);
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
        websocketClientEndpoint.connect();
    }


}
