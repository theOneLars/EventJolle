package ch.zuehlke.hatch.sailingserver.signalk.model.subscription;

public class SignalKSubscription extends AbstractSubscribtion {

    @Override
    public String getSubscriptionType() {
        return "subscribe";
    }

    protected SignalKSubscription() {
    }
}
