package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.domain.ApparentWindMeasurement
import ch.zuehlke.hatch.sailingserver.processing.ApparentWindSmoother
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class SmoothedApparentWindRepository(private val apparentWindRepository: ApparentWindRepository,
                                     private val apparentWindSmoother: ApparentWindSmoother) {

    fun getSmoothApparentWindStream(): Flux<ApparentWindMeasurement> {
        return apparentWindRepository.getApparentWindStream().map { apparentWindSmoother.smooth(it) }
    }

}