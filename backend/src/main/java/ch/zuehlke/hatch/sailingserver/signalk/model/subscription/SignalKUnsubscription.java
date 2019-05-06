package ch.zuehlke.hatch.sailingserver.signalk.model.subscription;

public class SignalKUnsubscription extends AbstractSubscribtion {

    public SignalKUnsubscription() {
    }

    @Override
    public String getSubscriptionType() {
        return "unsubscribe";
    }
}
