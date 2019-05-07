package ch.zuehlke.hatch.sailingserver.usecase.cockpit

import ch.zuehlke.hatch.sailingserver.domain.ApparentWindMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Heading
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import ch.zuehlke.hatch.sailingserver.domain.Wind
import ch.zuehlke.hatch.sailingserver.processing.ApparentWindSmoother
import ch.zuehlke.hatch.sailingserver.usecase.cockpit.model.CockpitDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class CockpitUseCase(val apparentWindSmoother: ApparentWindSmoother) {

    fun getCockpit(): Flux<CockpitDto> {

        val startTime = Instant.now().epochSecond

        val apparentWindStream = getMockApparentWindStream(startTime)

        return apparentWindStream.map { CockpitDto(it.wind, it.wind, 1.0, Heading(1.0), Heading(1.0)) }
    }

    private fun getMockApparentWindStream(startTime: Long): Flux<ApparentWindMeasurement> {
        return Flux
                .interval(Duration.ofMillis(1000))
                .map { tick ->
                    val randomSpeed = Math.random() * 10
                    val randomRadiant = Radiant(Math.random() * 2 * 3.14)
                    val timestamp = LocalDateTime.ofEpochSecond(startTime + tick.toInt(), 0, ZoneOffset.UTC)

                    ApparentWindMeasurement(Wind(speed = randomSpeed, radiant = randomRadiant), timestamp)

                }
                .map { apparentWindSmoother.smooth(it) }
    }
}