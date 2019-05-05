package ch.zuehlke.hatch.sailingserver.signalk.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Version {

    @JsonProperty("version")
    private String version;

    @JsonProperty("signalk-http")
    private String signalkhttp;

    @JsonProperty("signalk-ws")
    private String signalkws;

    @JsonProperty("signalk-tcp")
    private String signalktcp;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSignalkhttp() {
        return signalkhttp;
    }

    public void setSignalkhttp(String signalkhttp) {
        this.signalkhttp = signalkhttp;
    }

    public String getSignalkws() {
        return signalkws;
    }

    public void setSignalkws(String signalkws) {
        this.signalkws = signalkws;
    }

    public String getSignalktcp() {
        return signalktcp;
    }

    public void setSignalktcp(String signalktcp) {
        this.signalktcp = signalktcp;
    }

    @Override
    public String toString() {
        return "Version{" +
                "version='" + version + '\'' +
                ", signalkhttp='" + signalkhttp + '\'' +
                ", signalkws='" + signalkws + '\'' +
                ", signalktcp='" + signalktcp + '\'' +
                '}';
    }
}
