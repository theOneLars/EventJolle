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


    fun getTrueWindStream(): Flux<TrueWindMeasurement> {
        val measurementTimeout = 5000

        return Flux.combineLatest(
                Function { values: Array<Any> ->
                    TrueWindSourceMeasurements(values[0] as ApparentWindMeasurement,
                            values[1] as SpeedOverGroundMeasurement,
                            values[2] as CourseOverGroundMeasurement)
                },
                smoothedApparentWindRepository.getSmoothApparentWindStream(),
                speedOverGroundRepository.getSpeedOverGround(),
                courseOverGroundRepository.getCourseOverGround())
                .filter { it.getBiggestDelta() < measurementTimeout }
                .map {
                    val trueWind = TrueWind.from(it.speedOverGroundMeasurement.speed, it.courseOverGroundMeasurement.course, it.apparentWindMeasurement.wind)
                    TrueWindMeasurement(it.getNewest().timestamp, trueWind)
                }
    }

    private data class TrueWindSourceMeasurements(
            val apparentWindMeasurement: ApparentWindMeasurement,
            val speedOverGroundMeasurement: SpeedOverGroundMeasurement,
            val courseOverGroundMeasurement: CourseOverGroundMeasurement) : Measurements(apparentWindMeasurement, speedOverGroundMeasurement, courseOverGroundMeasurement)
}

