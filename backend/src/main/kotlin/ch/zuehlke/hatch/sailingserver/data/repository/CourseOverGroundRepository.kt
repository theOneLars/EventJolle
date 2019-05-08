package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.domain.CourseOverGroundMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Repository
class CourseOverGroundRepository {

    fun getMockCourseOverGround(): Flux<CourseOverGroundMeasurement> {
        val startTime = Instant.now().epochSecond
        return Flux
                .interval(Duration.ofMillis(1000))
                .map { tick ->
                    val randomRadiant = Radiant(Math.random() * 2 * 3.14)
                    val timestamp = LocalDateTime.ofEpochSecond(startTime + tick.toInt(), 0, ZoneOffset.UTC)

                    CourseOverGroundMeasurement(randomRadiant, timestamp)
                }
    }
}