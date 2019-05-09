package ch.zuehlke.hatch.sailingserver.usecase.cockpit

import ch.zuehlke.hatch.sailingserver.data.SmoothedApparentWindRepository
import ch.zuehlke.hatch.sailingserver.data.TrueWindRepository
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
                     val trueWindRepository: TrueWindRepository) {

    fun getCockpit(): Flux<CockpitDto> {

        return Flux.combineLatest(
                Function { values: Array<Any> ->
                    //TODO: find solution for "runtime" safe generic casting
                    val apparentWind = values[0] as MeasurementMessage<ApparentWindMeasurement>
                    val magneticHeading = values[1] as MeasurementMessage<MagneticHeadingMeasurement>
                    val speedOverGround = values[2] as MeasurementMessage<SpeedOverGroundMeasurement>
                    val courseOverGround = values[3] as MeasurementMessage<CourseOverGroundMeasurement>
                    val trueWind = values[4] as MeasurementMessage<TrueWindMeasurement>

                    if (apparentWind is MeasurementMessage.Data &&
                            magneticHeading is MeasurementMessage.Data &&
                            speedOverGround is MeasurementMessage.Data &&
                            courseOverGround is MeasurementMessage.Data &&
                            trueWind is MeasurementMessage.Data) {
                        CockpitDto(apparentWind.measurement.wind,
                                trueWind.measurement.trueWind.wind,
                                speedOverGround.measurement.speed,
                                courseOverGround.measurement.course,
                                magneticHeading.measurement.heading)
                    } else {
                        throw RuntimeException("data failure in get cockpit")
                    }
                },
                smoothedApparentWindRepository.getSmoothApparentWindStream(),
                magneticHeadingRepository.getMagneticHeading(),
                speedOverGroundRepository.getSpeedOverGround(),
                courseOverGroundRepository.getCourseOverGround(),
                trueWindRepository.getTrueWindStream())
    }

}
