package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.data.repository.CourseOverGroundRepository
import ch.zuehlke.hatch.sailingserver.data.repository.SpeedOverGroundRepository
import ch.zuehlke.hatch.sailingserver.domain.*
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.function.Function

@Repository
class TrueWindRepository(val smoothedApparentWindRepository: SmoothedApparentWindRepository,
                         val speedOverGroundRepository: SpeedOverGroundRepository,
                         val courseOverGroundRepository: CourseOverGroundRepository) {

    fun getTrueWindStream(): Flux<MeasurementMessage<TrueWindMeasurement>> {

        return Flux.combineLatest(
                Function { values: Array<Any> ->
                    //TODO: find solution for "runtime" safe generic casting
                    val apparentWind = values[0] as MeasurementMessage<ApparentWindMeasurement>
                    val speedOverGround = values[1] as MeasurementMessage<SpeedOverGroundMeasurement>
                    val courseOverGround = values[2] as MeasurementMessage<CourseOverGroundMeasurement>

                    if (apparentWind is MeasurementMessage.Data && speedOverGround is MeasurementMessage.Data && courseOverGround is MeasurementMessage.Data) {
                        val newestTimestamp = listOf(apparentWind.measurement.timestamp, speedOverGround.measurement.timestamp, courseOverGround.measurement.timestamp).max()
                         MeasurementMessage.Data(TrueWindMeasurement(newestTimestamp!!, TrueWind.from(speedOverGround.measurement.speed, courseOverGround.measurement.course, apparentWind.measurement.wind)))
                    } else {
                         MeasurementMessage.Failure<TrueWindMeasurement>("could not calculate true wind")
                    }
                },
                smoothedApparentWindRepository.getSmoothApparentWindStream(),
                speedOverGroundRepository.getSpeedOverGround(),
                courseOverGroundRepository.getCourseOverGround())
    }

}