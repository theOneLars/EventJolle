package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.data.repository.CourseOverGroundRepository
import ch.zuehlke.hatch.sailingserver.data.repository.MagneticHeadingRepository
import ch.zuehlke.hatch.sailingserver.data.repository.SpeedOverGroundRepository
import ch.zuehlke.hatch.sailingserver.domain.*
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.function.Function

@Repository
class TrueWindRepository(val smoothedApparentWindRepository: SmoothedApparentWindRepository,
                         val speedOverGroundRepository: SpeedOverGroundRepository,
                         val courseOverGroundRepository: CourseOverGroundRepository,
                         val magneticHeadingRepository: MagneticHeadingRepository) {


    fun getTrueWindStream(): Flux<TrueWindMeasurement> {
        val measurementTimeout = 5000

        return Flux.combineLatest(
                Function { values: Array<Any> ->
                    TrueWindSourceMeasurements(values[0] as ApparentWindMeasurement,
                            values[1] as SpeedOverGroundMeasurement,
                            values[2] as CourseOverGroundMeasurement,
                            values[3] as MagneticHeadingMeasurement)
                },
                smoothedApparentWindRepository.getSmoothApparentWindStream(),
                speedOverGroundRepository.getSpeedOverGround(),
                courseOverGroundRepository.getCourseOverGround(),
                magneticHeadingRepository.getMagneticHeading())
                .filter { it.getBiggestDelta() < measurementTimeout }
                .map {
                    val trueWind = TrueWind.from(it.speedOverGroundMeasurement.speed, it.courseOverGroundMeasurement.course, it.apparentWindMeasurement.wind, it.magneticHeadingMeasurement.heading)
                    TrueWindMeasurement(it.getNewest().timestamp, trueWind)
                }
    }

    private data class TrueWindSourceMeasurements(
            val apparentWindMeasurement: ApparentWindMeasurement,
            val speedOverGroundMeasurement: SpeedOverGroundMeasurement,
            val courseOverGroundMeasurement: CourseOverGroundMeasurement,
            val magneticHeadingMeasurement: MagneticHeadingMeasurement) : Measurements(apparentWindMeasurement, speedOverGroundMeasurement, courseOverGroundMeasurement, magneticHeadingMeasurement)
}

