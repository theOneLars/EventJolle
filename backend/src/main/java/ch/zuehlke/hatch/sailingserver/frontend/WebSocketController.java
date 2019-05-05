package ch.zuehlke.hatch.sailingserver.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import static ch.zuehlke.hatch.sailingserver.config.ApplicationConfig.MODEL_BROKER_PATH;
import static ch.zuehlke.hatch.sailingserver.config.ApplicationConfig.MODEL_BROKER_UPDATE_PATH;

@Controller
public class WebSocketController {

    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);

    private final SimpMessagingTemplate template;

    public WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Sends given object via websocket connection zto clients
     */
    public void publish(Object model) {
        this.template.convertAndSend(MODEL_BROKER_PATH + MODEL_BROKER_UPDATE_PATH, model);
    }

    @MessageMapping("/send/message")
    public void processMessageFromClient(@Payload String message) {
        log.info("Message arrived: " + message);
        // return message for test reasons
        this.template.convertAndSend(MODEL_BROKER_PATH + MODEL_BROKER_UPDATE_PATH, "Message reply from backend");
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        log.info("An error occurred: " + exception);
        return exception.getMessage();
    }

}
