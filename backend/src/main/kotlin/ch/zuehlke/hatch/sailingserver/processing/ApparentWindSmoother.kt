package ch.zuehlke.hatch.sailingserver.processing

import ch.zuehlke.hatch.sailingserver.domain.ApparentWindMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import ch.zuehlke.hatch.sailingserver.domain.Wind
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.time.temporal.ChronoUnit.SECONDS

class ApparentWindSmoother {

    val smoothRangeSeconds = 2
    var cache = mutableListOf<ApparentWindMeasurement>()

    fun smooth(inputWind: ApparentWindMeasurement): ApparentWindMeasurement {
        cache.add(inputWind)
        cache.retainAll { wind -> SECONDS.between(wind.timestamp, inputWind.timestamp) <= smoothRangeSeconds }

        val averageSpeed = cache
                .map({ it.wind.speed })
                .average()

        val averageRadiant = cache
                .map({ it.wind.radiant.value })
                .average()

        return ApparentWindMeasurement(Wind(averageSpeed, Radiant(averageRadiant)), inputWind.timestamp)
    }

}