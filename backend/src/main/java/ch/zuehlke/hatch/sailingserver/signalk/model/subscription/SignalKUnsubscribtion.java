package ch.zuehlke.hatch.sailingserver.signalk.model.subscription;

public class SignalKUnsubscribtion extends AbstractSubscribtion {

    public SignalKUnsubscribtion() {
    }

    @Override
    public String getSubscriptionType() {
        return "unsubscribe";
    }
}
