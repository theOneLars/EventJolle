package ch.zuehlke.hatch.sailingserver.signalk.model.info;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class ServerInfo {

    @JsonProperty("endpoints")
    private Endpoints endpoints;
    @JsonProperty("server")
    private Server server;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("endpoints")
    public Endpoints getEndpoints() {
        return endpoints;
    }

    @JsonProperty("endpoints")
    public void setEndpoints(Endpoints endpoints) {
        this.endpoints = endpoints;
    }

    @JsonProperty("server")
    public Server getServer() {
        return server;
    }

    @JsonProperty("server")
    public void setServer(Server server) {
        this.server = server;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getApiUrl() {
        return endpoints.getV1().getSignalkhttp();
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "endpoints=" + endpoints +
                ", server=" + server +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
