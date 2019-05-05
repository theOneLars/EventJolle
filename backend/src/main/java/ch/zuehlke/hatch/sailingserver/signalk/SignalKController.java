package ch.zuehlke.hatch.sailingserver.signalk;

import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalKSubscibtionFactory;
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalKSubscription;
import ch.zuehlke.hatch.sailingserver.signalk.model.subscription.SignalKSubscriptionPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SignalKController {

    private static final Logger log = LoggerFactory.getLogger(SignalKService.class);

    SignalKService signalKService;

    @Autowired
    public SignalKController(SignalKService signalKService) {
        this.signalKService = signalKService;
        log.info("Server info from SignalK server: " + getFullServerInfo());
        startWebsocketConnection();
        SignalKSubscription subscription = SignalKSubscibtionFactory.createDefaultSubscriptionWithSinglePath(
                "*",
                2000,
                SignalKSubscriptionPath.FORMAT_DELTA,
                SignalKSubscriptionPath.POLICY_INSTANT,
                2000);
        subscribeToSignalKServer(subscription);
    }

    public Object getFullServerInfo() {
        return signalKService.getFullServerInfo();
    }

    public void startWebsocketConnection() {
        signalKService.startWebsocketConection();
    }

    public void subscribeToSignalKServer(SignalKSubscription subscription) {
        signalKService.subscribeToSignalKServer(subscription);
    }
}
