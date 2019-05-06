package ch.zuehlke.hatch.sailingserver.processing

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.time.temporal.ChronoUnit.SECONDS

@Service
@Scope("prototype")
class ApparentWindSmoother {

    val smoothRangeSeconds = 2
    var cache = mutableListOf<ApparentWind>()

    fun smooth(inputWind: ApparentWind): ApparentWind {
        cache.add(inputWind)
        cache.retainAll { wind -> SECONDS.between(wind.timestamp, inputWind.timestamp) <= smoothRangeSeconds }

        val averageSpeed = cache
                .map({ it.speed })
                .average()

        return inputWind.copy(speed = averageSpeed)
    }

}