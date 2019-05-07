package ch.zuehlke.hatch.sailingserver.processing

import ch.zuehlke.hatch.sailingserver.domain.ApparentWindMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import ch.zuehlke.hatch.sailingserver.domain.Wind
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime


internal class ApparentWindSmootherTest {

    @Test
    fun cacheWithOldValues_smooth_cacheContainsNoWindEntriesOlderThanTwoSeconds() {
        val testee = ApparentWindSmoother()
        testee.cache = mutableListOf(
                ApparentWindMeasurement(Wind(7.1404906978132, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 0, 0)),
                ApparentWindMeasurement(Wind(4.815201219850976, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 1, 0)),
                ApparentWindMeasurement(Wind(5.453112492566276, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 2, 0)),
                ApparentWindMeasurement(Wind(6.056565645616007, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 3, 0))
        )

        testee.smooth(ApparentWindMeasurement(Wind(7.022168445616007, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 4, 0)))

        assertThat(testee.cache).containsExactly(
                ApparentWindMeasurement(Wind(5.453112492566276, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 2, 0)),
                ApparentWindMeasurement(Wind(6.056565645616007, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 3, 0)),
                ApparentWindMeasurement(Wind(7.022168445616007, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 4, 0))
        )
    }

    @Test
    fun cacheWithMultipleValuesWithinOneSecond_smooth_cacheContainsNoWindEntriesOlderThanTwoSeconds() {
        val testee = ApparentWindSmoother()
        testee.cache = mutableListOf(
                ApparentWindMeasurement(Wind(7.1404906978132, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 1, 1)),
                ApparentWindMeasurement(Wind(4.815201219850976, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 1, 232)),
                ApparentWindMeasurement(Wind(5.453112492566276, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 1, 432)),
                ApparentWindMeasurement(Wind(6.056565645616007, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 1, 1)))

        val result = testee.smooth(ApparentWindMeasurement(Wind(7.022168445616007, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 2, 0)))

        assertThat(testee.cache).containsExactly(
                ApparentWindMeasurement(Wind(7.1404906978132, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 1, 1)),
                ApparentWindMeasurement(Wind(4.815201219850976, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 1, 232)),
                ApparentWindMeasurement(Wind(5.453112492566276, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 1, 432)),
                ApparentWindMeasurement(Wind(6.056565645616007, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 1, 1)),
                ApparentWindMeasurement(Wind(7.022168445616007, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 2, 0)))

        assertThat(result.wind.speed).isEqualTo(6.097507700292494)

    }

    @Test
    fun cacheWithOldValues_smooth_averageOfAllWindValuesWithinTheLastTwoSeconds() {
        val testee = ApparentWindSmoother()
        testee.cache = mutableListOf(
                ApparentWindMeasurement(Wind(5.453112492566276, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 2, 0)),
                ApparentWindMeasurement(Wind(6.056565645616007, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 3, 0))
        )

        val result = testee.smooth(ApparentWindMeasurement(Wind(7.022168445616007, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 4, 0)))

        assertThat(result).isEqualTo(ApparentWindMeasurement(Wind(6.177282194599431, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 4, 0)))
    }
}