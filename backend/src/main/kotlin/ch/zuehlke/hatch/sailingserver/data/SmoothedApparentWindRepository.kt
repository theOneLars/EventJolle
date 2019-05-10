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
                .map { message ->
                    when (message) {
                        is MeasurementMessage.Data -> message.measurement
                        else -> throw RuntimeException("error handling not yet implemented")
                    }
                }
                .map { smoother.smooth(it) }
                .map { MeasurementMessage.Data(it) }
    }

}