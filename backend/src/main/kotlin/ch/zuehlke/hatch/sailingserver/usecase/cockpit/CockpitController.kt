package ch.zuehlke.hatch.sailingserver.usecase.cockpit

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

import java.time.Duration

@RestController
@RequestMapping("/cockpit")
class CockpitController {


    private val cockpit: Flux<String>
        @GetMapping(path = [""], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
        get() = Flux
                .interval(Duration.ofMillis(2000))
                .map { tick -> "cockpit data " + tick!! }
}

