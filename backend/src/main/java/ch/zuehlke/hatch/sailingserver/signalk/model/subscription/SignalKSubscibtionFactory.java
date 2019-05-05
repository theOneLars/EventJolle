package ch.zuehlke.hatch.sailingserver.signalk.model.subscription;

public class SignalKSubscibtionFactory {

    public static SignalKSubscription createDefaultSubscription() {
        SignalKSubscription subscription = new SignalKSubscription();
        subscription.setContext(SignalKSubscription.DEFAULT_CONTEXT);
        return subscription;
    }

    public static SignalKSubscription createDefaultSubscriptionWithSinglePath(String path, Integer period, String format,
            String policy, Integer minPeriod) {

        SignalKSubscription subscription = createDefaultSubscription();

        SignalKSubscriptionPath subscriptionPathpath = new SignalKSubscriptionPath(path, period, minPeriod, format, policy);
        subscription.addPath(subscriptionPathpath);

        return subscription;
    }

    public static SignalKSubscription createDefaultSubscriptionWithDefaultSinglePath() {
        return createDefaultSubscriptionWithSinglePath(null, null, null, null, null);
    }

}
