package ch.zuehlke.hatch.sailingserver.usecase.cockpit

import ch.zuehlke.hatch.sailingserver.processing.ApparentWind
import ch.zuehlke.hatch.sailingserver.processing.ApparentWindSmoother
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Service
class CockpitUseCase(val apparentWindSmoother: ApparentWindSmoother) {

    fun getCockpit(): Flux<ApparentWind> {
        return Flux.just<ApparentWind>(
                ApparentWind(7.1404906978132, LocalDateTime.of(2017, 1, 7, 12, 4, 1, 1)),
                ApparentWind(4.815201219850976, LocalDateTime.of(2017, 1, 7, 12, 4, 3, 232)),
                ApparentWind(5.453112492566276, LocalDateTime.of(2017, 1, 7, 12, 4, 5, 432)),
                ApparentWind(6.056565645616007, LocalDateTime.of(2017, 1, 7, 12, 4, 6, 1)),
                ApparentWind(7.022168445616007, LocalDateTime.of(2017, 1, 7, 12, 4, 9, 0)))
                .map { apparentWindSmoother.smooth(it) }
    }
}