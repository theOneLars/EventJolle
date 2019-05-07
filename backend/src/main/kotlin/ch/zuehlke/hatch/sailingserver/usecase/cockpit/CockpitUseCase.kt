package ch.zuehlke.hatch.sailingserver.usecase.cockpit

import ch.zuehlke.hatch.sailingserver.domain.ApparentWindMeasurement
import ch.zuehlke.hatch.sailingserver.domain.MagneticHeadingMeasurement
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
import java.util.function.Function

@Service
class CockpitUseCase(val apparentWindSmoother: ApparentWindSmoother) {

    fun getCockpit(): Flux<CockpitDto> {
        val apparentWindStream = processApparentWindStream()

        val mockMagneticHeadingStream = getMockMagneticHeadingStream()

        return Flux.combineLatest(
                Function { values: Array<Any> ->
                    val apparentWind = values[0] as ApparentWindMeasurement
                    val magneticHeading = values[1] as MagneticHeadingMeasurement
                    CockpitDto(apparentWind.wind, apparentWind.wind, 1.0, Radiant(1.0), magneticHeading.heading)
                },
                apparentWindStream, mockMagneticHeadingStream)
    }

    private fun processApparentWindStream(): Flux<ApparentWindMeasurement> {
        return getMockApparentWindStream()
                .map { apparentWindSmoother.smooth(it) }
    }

    private fun getMockApparentWindStream(): Flux<ApparentWindMeasurement> {
        val startTime = Instant.now().epochSecond
        return Flux
                .interval(Duration.ofMillis(1000))
                .map { tick ->
                    val randomSpeed = Math.random() * 10
                    val randomRadiant = Radiant(Math.random() * 2 * 3.14)
                    val timestamp = LocalDateTime.ofEpochSecond(startTime + tick.toInt(), 0, ZoneOffset.UTC)

                    ApparentWindMeasurement(Wind(speed = randomSpeed, radiant = randomRadiant), timestamp)

                }
    }

    private fun getMockMagneticHeadingStream(): Flux<MagneticHeadingMeasurement> {
        val startTime = Instant.now().epochSecond
        return Flux
                .interval(Duration.ofMillis(1000))
                .map { tick ->
                    val randomRadiant = Radiant(Math.random() * 2 * 3.14)
                    val timestamp = LocalDateTime.ofEpochSecond(startTime + tick.toInt(), 0, ZoneOffset.UTC)

                    MagneticHeadingMeasurement(randomRadiant, timestamp)

                }
    }
}