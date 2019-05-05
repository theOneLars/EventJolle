package ch.zuehlke.hatch.sailingserver.signalk.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Endpoints {

    @JsonProperty("v1")
    private Version v1;

    public Version getV1() {
        return v1;
    }

    public void setV1(Version v1) {
        this.v1 = v1;
    }

    @Override
    public String toString() {
        return "Endpoints{" +
                "v1=" + v1 +
                '}';
    }
}
