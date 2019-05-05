package ch.zuehlke.hatch.sailingserver.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
@ComponentScan(basePackages = "ch.zuehlke.hatch.sailingserver")
@EnableWebSocket
public class ApplicationConfig implements WebSocketMessageBrokerConfigurer {

    public final static String MODEL_BROKER_PATH = "/model";
    public final static String MODEL_BROKER_UPDATE_PATH = "/update";

    public final static String ENDPOINT_SOCKET_PATH = "/socket";
    public final static String APPLICATION_DESTINATION_PREFIX = "/app";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(MODEL_BROKER_PATH);
        config.setApplicationDestinationPrefixes(APPLICATION_DESTINATION_PREFIX);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(ENDPOINT_SOCKET_PATH)
                .setAllowedOrigins("*");
    }
}