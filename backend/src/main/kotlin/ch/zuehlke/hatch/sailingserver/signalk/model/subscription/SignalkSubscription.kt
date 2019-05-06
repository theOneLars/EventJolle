package ch.zuehlke.hatch.sailingserver.signalk.model.subscription

data class SubscriptionInfo(val path: String, val period: String, val format: String, val policy: String, val minPeriod: String)

data class SignalkSubscription(val context: String, val subscribe: List<SubscriptionInfo>)