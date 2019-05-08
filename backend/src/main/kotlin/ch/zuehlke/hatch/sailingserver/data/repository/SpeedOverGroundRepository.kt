package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.domain.SpeedOverGroundMeasurement
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Repository
class SpeedOverGroundRepository {

    fun getMockSpeedOverGround(): Flux<SpeedOverGroundMeasurement> {
        val startTime = Instant.now().epochSecond
        return Flux
                .interval(Duration.ofMillis(1000))
                .map { tick ->
                    val randomSpeed = Math.random() * 5
                    val timestamp = LocalDateTime.ofEpochSecond(startTime + tick.toInt(), 0, ZoneOffset.UTC)

                    SpeedOverGroundMeasurement(randomSpeed, timestamp)
                }
    }

}