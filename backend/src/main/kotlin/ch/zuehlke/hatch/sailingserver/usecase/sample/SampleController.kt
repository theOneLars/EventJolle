package ch.zuehlke.hatch.sailingserver.usecase.sample

import ch.zuehlke.hatch.sailingserver.data.PositionRepository
import ch.zuehlke.hatch.sailingserver.domain.Position
import ch.zuehlke.hatch.sailingserver.signalk.SignalKService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDateTime

@RestController
@RequestMapping("/sample")
class SampleController {

    private val repository: PositionRepository

    constructor(signalKService: SignalKService, repository: PositionRepository) {
        signalKService.getFullServerInfo()
        signalKService.startWebsocketConection()
        this.repository = repository
    }

    private val sample: Mono<String>
        @GetMapping
        get() = Mono.just("sample string")

    private val sampleFlux: Flux<String>
        @GetMapping(path = ["/flux"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
        get() = Flux
                .interval(Duration.ofMillis(2000))
                .map { tick -> "string " + tick!! }

    @GetMapping(path = ["/repoFlux"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getPositions(@RequestParam("from")
                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                     from: LocalDateTime,
                     @RequestParam("to")
                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                     to: LocalDateTime): Flux<Position> {
        return this.repository.getPositions(from, to);
    }

}

