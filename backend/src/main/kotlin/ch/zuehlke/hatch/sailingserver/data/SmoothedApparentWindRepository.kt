package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.domain.ApparentWindMeasurement
import ch.zuehlke.hatch.sailingserver.domain.MeasurementMessage
import ch.zuehlke.hatch.sailingserver.processing.ApparentWindSmoother
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class SmoothedApparentWindRepository(private val apparentWindRepository: ApparentWindRepository) {

    fun getSmoothApparentWindStream(): Flux<MeasurementMessage<ApparentWindMeasurement>> {
        val smoother = ApparentWindSmoother()
        return apparentWindRepository.getApparentWindStream()
                .map { mapOrPropagateError(it, smoother::smooth) }
    }


}