package ch.zuehlke.hatch.sailingserver.usecase.sample;

import ch.zuehlke.hatch.sailingserver.signalk.SignalKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/sample")
public class SampleController {

    private SignalKService signalKService;

    @Autowired
    public SampleController(SignalKService signalKService) {
        this.signalKService = signalKService;

        signalKService.getFullServerInfo();
        signalKService.startWebsocketConection();
    }

    @GetMapping()
    private Mono<String> getSample() {
        return Mono.just("sample string");
    }

    @GetMapping(path="/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<String> getSampleFlux() {
        return Flux
                .interval(Duration.ofMillis(2000))
                .map((tick) -> "string " + tick);
    }
}

