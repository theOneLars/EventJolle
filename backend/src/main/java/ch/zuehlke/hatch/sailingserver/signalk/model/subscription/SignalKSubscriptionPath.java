package ch.zuehlke.hatch.sailingserver.signalk.model.subscription;

import org.springframework.util.StringUtils;

public class SignalKSubscriptionPath {

    public final static String FORMAT_DELTA = "delta";
    public final static String FORMAT_FULL = "full";

    public final static String POLICY_INSTANT = "instant";
    public final static String POLICY_IDEAL = "ideal";
    public final static String POLICY_FIXED = "fixed";

    private String path;
    private Integer period;
    private Integer minPeriod;
    private String format;
    private String policy;

    public SignalKSubscriptionPath(String path, Integer period, Integer minPeriod, String format, String policy) {
        this.path = path;
        this.period = period;
        this.minPeriod = minPeriod;
        this.format = format;
        this.policy = policy;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getMinPeriod() {
        return minPeriod;
    }

    public void setMinPeriod(int minPeriod) {
        this.minPeriod = minPeriod;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("{ ");

        // path
        path = !StringUtils.isEmpty(path) ? path : "*";
        builder.append("\"path\": \"" + path + "\",");

        // period
        period = period != null ? period : 1000;
        builder.append("\"period\": \"" + period + "\",");

        // format
        if (StringUtils.isEmpty(format) || (!FORMAT_DELTA.equals(format) && !FORMAT_FULL.equals(format))) {
            format = FORMAT_DELTA;
        }
        builder.append("\"format\": \"" + format + "\",");

        // policy
        if (StringUtils.isEmpty(policy) || (!POLICY_FIXED.equals(policy) && !POLICY_IDEAL.equals(policy) && !POLICY_INSTANT.equals(policy))) {
            policy = POLICY_INSTANT;
        }
        builder.append("\"policy\": \"" + policy + "\",");

        // minPeriod
        minPeriod = minPeriod != null ? minPeriod : 1000;
        builder.append("\"minPeriod\": \"" + minPeriod + "\"");

        builder.append("}");

        return builder.toString();
    }
}
