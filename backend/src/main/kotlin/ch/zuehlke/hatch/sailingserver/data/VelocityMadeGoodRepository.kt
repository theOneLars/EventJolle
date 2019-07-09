package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.data.repository.SpeedOverGroundRepository
import ch.zuehlke.hatch.sailingserver.domain.Measurements
import ch.zuehlke.hatch.sailingserver.domain.SpeedOverGroundMeasurement
import ch.zuehlke.hatch.sailingserver.domain.TrueWindMeasurement
import ch.zuehlke.hatch.sailingserver.domain.VelocityMadeGoodMeasurement
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.function.Function
import kotlin.math.cos

@Repository
class VelocityMadeGoodRepository(val trueWindRepository: TrueWindRepository, val speedOverGroundRepository: SpeedOverGroundRepository) {
    val measurementTimeout = 5000

    fun getVelocityMadeGoodStream(): Flux<VelocityMadeGoodMeasurement> {

        return Flux.combineLatest(
                Function { values: Array<Any> ->
                    VelocityMadeGoodMeasurements(values[0] as TrueWindMeasurement,
                            values[1] as SpeedOverGroundMeasurement)
                },
                trueWindRepository.getTrueWindStream(),
                speedOverGroundRepository.getSpeedOverGround())
                .filter { it.getBiggestDelta() > measurementTimeout }
                .map {
                    val velocityMadeGood = cos(it.trueWindMeasurement.trueWind.angle.value) * it.speedOverGroundMeasurement.speed
                    VelocityMadeGoodMeasurement(it.getNewest().timestamp, velocityMadeGood)
                }
    }

    private data class VelocityMadeGoodMeasurements(
            val trueWindMeasurement: TrueWindMeasurement,
            val speedOverGroundMeasurement: SpeedOverGroundMeasurement) : Measurements(trueWindMeasurement, speedOverGroundMeasurement)
}
