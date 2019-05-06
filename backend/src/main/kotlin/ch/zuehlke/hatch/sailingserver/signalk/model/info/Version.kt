package ch.zuehlke.hatch.sailingserver.signalk.model.info

import com.fasterxml.jackson.annotation.JsonProperty

data class Version(
        val version: String,
        @JsonProperty("signalk-http") val signalkhttp: String,
        @JsonProperty("signalk-ws") val signalkws: String,
        @JsonProperty("signalk-tcp") val signalktcp: String)