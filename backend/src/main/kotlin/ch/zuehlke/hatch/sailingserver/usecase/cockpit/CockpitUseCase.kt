package ch.zuehlke.hatch.sailingserver.usecase.cockpit

import ch.zuehlke.hatch.sailingserver.data.SmoothedApparentWindRepository
import ch.zuehlke.hatch.sailingserver.data.repository.CourseOverGroundRepository
import ch.zuehlke.hatch.sailingserver.data.repository.MagneticHeadingRepository
import ch.zuehlke.hatch.sailingserver.data.repository.SpeedOverGroundRepository
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindMeasurement
import ch.zuehlke.hatch.sailingserver.domain.CourseOverGroundMeasurement
import ch.zuehlke.hatch.sailingserver.domain.MagneticHeadingMeasurement
import ch.zuehlke.hatch.sailingserver.domain.SpeedOverGroundMeasurement
import ch.zuehlke.hatch.sailingserver.usecase.cockpit.model.CockpitDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.function.Function

@Service
class CockpitUseCase(val smoothedApparentWindRepository: SmoothedApparentWindRepository,
                     val magneticHeadingRepository: MagneticHeadingRepository,
                     val speedOverGroundRepository: SpeedOverGroundRepository,
                     val courseOverGroundRepository: CourseOverGroundRepository) {

    fun getCockpit(): Flux<CockpitDto> {

        val smoothApparentWindStream = smoothedApparentWindRepository.getSmoothApparentWindStream()

        val magneticHeadingStream = magneticHeadingRepository.getMockMagneticHeadingStream()

        val speedOverGroundStream = speedOverGroundRepository.getMockSpeedOverGround()

        val courseOverGroundStream = courseOverGroundRepository.getMockCourseOverGround()

        return Flux.combineLatest(
                Function { values: Array<Any> ->
                    val apparentWind = values[0] as ApparentWindMeasurement
                    val magneticHeading = values[1] as MagneticHeadingMeasurement
                    val speedOverGround = values[2] as SpeedOverGroundMeasurement
                    val courseOverGround = values[3] as CourseOverGroundMeasurement
                    CockpitDto(apparentWind.wind, apparentWind.wind, speedOverGround.speed, courseOverGround.course, magneticHeading.heading)
                },
                smoothApparentWindStream,
                magneticHeadingStream,
                speedOverGroundStream,
                courseOverGroundStream)
    }

}
