package ch.zuehlke.hatch.sailingserver.signalk.model.subscription;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSubscribtion {

    public static final String DEFAULT_CONTEXT = "vessels.self";

    private String context;
    private List<SignalKSubscriptionPath> pathList = new ArrayList<>();

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<SignalKSubscriptionPath> getPathList() {
        return pathList;
    }

    public void setPathList(List<SignalKSubscriptionPath> pathList) {
        this.pathList = pathList;
    }

    public void addPath(SignalKSubscriptionPath path) {
        pathList.add(path);
    }

    public abstract String getSubscriptionType();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(
                "{ " +
                        "\"context\": \"" + context + "\"," +
                        "\"" + getSubscriptionType() + "\": [");
        for (SignalKSubscriptionPath item: pathList) {
            builder.append(item);
        }
        builder.append("]}");

        return builder.toString();
    }
}
