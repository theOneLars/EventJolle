package ch.zuehlke.hatch.sailingserver.signalk.model.subscription;

public class SignalKSubscriptionFactory {

    public static SignalKSubscription createDefaultSubscriptionWithSinglePath(String path, Integer period, String format,
            String policy, Integer minPeriod) {

        SignalKSubscription subscription = createDefaultSubscription();

        SignalKSubscriptionPath subscriptionPath = new SignalKSubscriptionPath(path, period, minPeriod, format, policy);
        subscription.addPath(subscriptionPath);

        return subscription;
    }

    private static SignalKSubscription createDefaultSubscription() {
        SignalKSubscription subscription = new SignalKSubscription();
        subscription.setContext(SignalKSubscription.DEFAULT_CONTEXT);
        return subscription;
    }
}
