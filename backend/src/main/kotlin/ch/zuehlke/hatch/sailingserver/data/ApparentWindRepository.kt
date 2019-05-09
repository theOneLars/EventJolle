package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.data.repository.ApparentWindAngleRepository
import ch.zuehlke.hatch.sailingserver.data.repository.ApparentWindSpeedRepository
import ch.zuehlke.hatch.sailingserver.domain.*
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.function.Function

@Repository
class ApparentWindRepository(
        private val apparentWindAngleRepository: ApparentWindAngleRepository,
        private val apparentWindSpeedRepository: ApparentWindSpeedRepository
) {

    fun getApparentWindStream(): Flux<ApparentWindMeasurement> {
        return Flux.combineLatest(
                Function { values: Array<Any> ->
                    val apparentWindAngle = values[0] as ApparentWindAngleMeasurement
                    val apparentWindSpeed = values[1] as ApparentWindSpeedMeasurement

                    val newestTimestamp = listOf(apparentWindAngle.timestamp, apparentWindSpeed.timestamp).max()

                    ApparentWindMeasurement(
                            Wind(apparentWindSpeed.speed, apparentWindAngle.radiant),
                            newestTimestamp!!
                    )
                },
                this.apparentWindAngleRepository.getApparentWindAngles(),
                this.apparentWindSpeedRepository.getApparentWindSpeeds()
        );
    }
}