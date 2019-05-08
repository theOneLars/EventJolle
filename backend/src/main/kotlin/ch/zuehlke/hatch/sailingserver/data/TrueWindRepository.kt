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

        return Flux.combineLatest(
                Function { values: Array<Any> ->
                    val apparentWind = values[0] as ApparentWindMeasurement
                    val speedOverGround = values[1] as SpeedOverGroundMeasurement
                    val courseOverGround = values[2] as CourseOverGroundMeasurement

                    val newestTimestamp = listOf(apparentWind.timestamp, speedOverGround.timestamp, courseOverGround.timestamp).max()

                    TrueWindMeasurement(newestTimestamp!!, TrueWind.from(speedOverGround.speed, courseOverGround.course, apparentWind.wind))
                },
                smoothedApparentWindRepository.getSmoothApparentWindStream(),
                speedOverGroundRepository.getMockSpeedOverGround(),
                courseOverGroundRepository.getMockCourseOverGround())
    }

}