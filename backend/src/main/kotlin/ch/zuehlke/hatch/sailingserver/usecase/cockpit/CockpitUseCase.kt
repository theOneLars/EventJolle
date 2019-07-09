package ch.zuehlke.hatch.sailingserver.usecase.cockpit

import ch.zuehlke.hatch.sailingserver.data.SmoothedApparentWindRepository
import ch.zuehlke.hatch.sailingserver.data.TrueWindRepository
import ch.zuehlke.hatch.sailingserver.data.VelocityMadeGoodRepository
import ch.zuehlke.hatch.sailingserver.data.repository.CourseOverGroundRepository
import ch.zuehlke.hatch.sailingserver.data.repository.MagneticHeadingRepository
import ch.zuehlke.hatch.sailingserver.data.repository.SpeedOverGroundRepository
import ch.zuehlke.hatch.sailingserver.domain.*
import ch.zuehlke.hatch.sailingserver.usecase.cockpit.model.CockpitDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.function.Function

@Service
class CockpitUseCase(val smoothedApparentWindRepository: SmoothedApparentWindRepository,
                     val magneticHeadingRepository: MagneticHeadingRepository,
                     val speedOverGroundRepository: SpeedOverGroundRepository,
                     val courseOverGroundRepository: CourseOverGroundRepository,
                     val trueWindRepository: TrueWindRepository,
                     val velocityMadeGoodRepository: VelocityMadeGoodRepository) {

    fun getCockpit(): Flux<CockpitDto> {

        return Flux.combineLatest(
                Function { values: Array<Any> ->
                    val apparentWind = values[0] as ApparentWindMeasurement
                    val magneticHeading = values[1] as MagneticHeadingMeasurement
                    val speedOverGround = values[2] as SpeedOverGroundMeasurement
                    val courseOverGround = values[3] as CourseOverGroundMeasurement
                    val trueWind = values[4] as TrueWindMeasurement
                    val velocityMadeGood = values[5] as VelocityMadeGoodMeasurement
                    CockpitDto(apparentWind.wind, trueWind.trueWind, speedOverGround.speed, courseOverGround.course, magneticHeading.heading, velocityMadeGood.velocityMadeGood)
                },
                smoothedApparentWindRepository.getSmoothApparentWindStream(),
                magneticHeadingRepository.getMagneticHeading(),
                speedOverGroundRepository.getSpeedOverGround(),
                courseOverGroundRepository.getCourseOverGround(),
                trueWindRepository.getTrueWindStream(),
                velocityMadeGoodRepository.getVelocityMadeGoodStream())
    }
}
