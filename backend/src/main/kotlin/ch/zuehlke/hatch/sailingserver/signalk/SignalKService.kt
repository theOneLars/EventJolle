package ch.zuehlke.hatch.sailingserver.signalk

import ch.zuehlke.hatch.sailingserver.signalk.model.info.ServerInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.IOException

@Service
class SignalKService(
        val websocketClientEndpoint: WebsocketClientEndpoint,
        @param:Value("\${signalk.rest.endpoint}")
        val signalkRestEndpoint: String) {

    companion object {
        private val log = LoggerFactory.getLogger(SignalKService::class.java)
    }

    fun getFullServerInfo(): ServerInfo {
        val restTemplate = RestTemplate()
        println(signalkRestEndpoint)
        val response = restTemplate.getForEntity(signalkRestEndpoint, String::class.java)

        try {
            val mapper = jacksonObjectMapper()
            val serverInfo = mapper.readValue(response.body, ServerInfo::class.java)
            log.debug("Fetched Info from Server")
            return serverInfo
        } catch (e: IOException) {
            log.error("Could not read ServerInfo from SignalK Server", e)
        }

        throw IllegalStateException("Could not connect to signalK server")
    }

    fun startWebsocketConection() {
        websocketClientEndpoint.connect()
    }

}
