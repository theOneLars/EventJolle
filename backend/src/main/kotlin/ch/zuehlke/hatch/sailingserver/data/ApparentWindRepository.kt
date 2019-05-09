package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.data.repository.ApparentWindAngleRepository
import ch.zuehlke.hatch.sailingserver.data.repository.ApparentWindSpeedRepository
import ch.zuehlke.hatch.sailingserver.domain.*
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.function.Function

@Repository
class ApparentWindRepository(
        private val apparentWindAngleRepository: ApparentWindAngleRepository,
        private val apparentWindSpeedRepository: ApparentWindSpeedRepository
) {

    fun getApparentWindStream(): Flux<MeasurementMessage<ApparentWindMeasurement>> {
        return Flux.combineLatest(
                Function { values: Array<Any> ->
                    val apparentWindAngle = values[0] as MeasurementMessage<ApparentWindAngleMeasurement>
                    val apparentWindSpeed = values[1] as MeasurementMessage<ApparentWindSpeedMeasurement>

                    if (apparentWindAngle is MeasurementMessage.Data && apparentWindSpeed is MeasurementMessage.Data) {
                        val newestTimestamp = listOf(apparentWindAngle.measurement.timestamp, apparentWindSpeed.measurement.timestamp).max()

                        MeasurementMessage.Data(ApparentWindMeasurement(
                                Wind(apparentWindSpeed.measurement.speed, apparentWindAngle.measurement.radiant),
                                newestTimestamp!!
                        ))
                    } else {
                        MeasurementMessage.Failure<ApparentWindMeasurement>("illegal source data for apparentWindStream")
                    }
                },
                this.apparentWindAngleRepository.getApparentWindAngles(),
                this.apparentWindSpeedRepository.getApparentWindSpeeds()
        )
    }
}