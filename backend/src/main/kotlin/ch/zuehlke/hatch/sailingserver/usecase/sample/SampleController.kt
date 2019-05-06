package ch.zuehlke.hatch.sailingserver.usecase.sample

import ch.zuehlke.hatch.sailingserver.signalk.SignalKService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
@RequestMapping("/sample")
class SampleController(signalKService: SignalKService) {

    private val sample: Mono<String>
        @GetMapping
        get() = Mono.just("sample string")

    private val sampleFlux: Flux<String>
        @GetMapping(path = ["/flux"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
        get() = Flux
                .interval(Duration.ofMillis(2000))
                .map { tick -> "string " + tick!! }

    init {
        signalKService.getFullServerInfo()
        signalKService.startWebsocketConection()
    }
}

