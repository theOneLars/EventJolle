package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.domain.ApparentWindMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import ch.zuehlke.hatch.sailingserver.domain.Wind
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Repository
class ApparentWindRepository {

    fun getMockApparentWindStream(): Flux<ApparentWindMeasurement> {
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
}