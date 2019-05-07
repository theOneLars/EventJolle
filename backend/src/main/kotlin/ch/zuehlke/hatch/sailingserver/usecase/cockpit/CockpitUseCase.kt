package ch.zuehlke.hatch.sailingserver.usecase.cockpit

import ch.zuehlke.hatch.sailingserver.data.repository.ApparentWindRepository
import ch.zuehlke.hatch.sailingserver.data.repository.MagneticHeadingRepository
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindMeasurement
import ch.zuehlke.hatch.sailingserver.domain.MagneticHeadingMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import ch.zuehlke.hatch.sailingserver.processing.ApparentWindSmoother
import ch.zuehlke.hatch.sailingserver.usecase.cockpit.model.CockpitDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.function.Function

@Service
class CockpitUseCase(val apparentWindRepository: ApparentWindRepository,
                     val magneticHeadingRepository: MagneticHeadingRepository,
                     val apparentWindSmoother: ApparentWindSmoother) {

    fun getCockpit(): Flux<CockpitDto> {

        val apparentWindStream = apparentWindRepository.getMockApparentWindStream()
                .map { apparentWindSmoother.smooth(it) }

        val magneticHeadingStream = magneticHeadingRepository.getMockMagneticHeadingStream()

        return Flux.combineLatest(
                Function { values: Array<Any> ->
                    val apparentWind = values[0] as ApparentWindMeasurement
                    val magneticHeading = values[1] as MagneticHeadingMeasurement
                    CockpitDto(apparentWind.wind, apparentWind.wind, 1.0, Radiant(1.0), magneticHeading.heading)
                },
                apparentWindStream,
                magneticHeadingStream)
    }

}
