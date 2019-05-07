package ch.zuehlke.hatch.sailingserver.processing

import ch.zuehlke.hatch.sailingserver.domain.ApparentWind
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime


internal class ApparentWindSmootherTest {

    @Test
    fun cacheWithOldValues_smooth_cacheContainsNoWindEntriesOlderThanTwoSeconds() {
        val testee = ApparentWindSmoother()
        testee.cache = mutableListOf(
                ApparentWind(7.1404906978132, LocalDateTime.of(2017, 1, 7, 12, 4, 0, 0)),
                ApparentWind(4.815201219850976, LocalDateTime.of(2017, 1, 7, 12, 4, 1, 0)),
                ApparentWind(5.453112492566276, LocalDateTime.of(2017, 1, 7, 12, 4, 2, 0)),
                ApparentWind(6.056565645616007, LocalDateTime.of(2017, 1, 7, 12, 4, 3, 0)
                )
        )

        testee.smooth(ApparentWind(7.022168445616007, LocalDateTime.of(2017, 1, 7, 12, 4, 4, 0)))

        assertThat(testee.cache).containsExactly(
                ApparentWind(5.453112492566276, LocalDateTime.of(2017, 1, 7, 12, 4, 2, 0)),
                ApparentWind(6.056565645616007, LocalDateTime.of(2017, 1, 7, 12, 4, 3, 0)),
                ApparentWind(7.022168445616007, LocalDateTime.of(2017, 1, 7, 12, 4, 4, 0))
        )
    }

    @Test
    fun cacheWithMultipleValuesWithinOneSecond_smooth_cacheContainsNoWindEntriesOlderThanTwoSeconds() {
        val testee = ApparentWindSmoother()
        testee.cache = mutableListOf(
                ApparentWind(7.1404906978132, LocalDateTime.of(2017, 1, 7, 12, 4, 1, 1)),
                ApparentWind(4.815201219850976, LocalDateTime.of(2017, 1, 7, 12, 4, 1, 232)),
                ApparentWind(5.453112492566276, LocalDateTime.of(2017, 1, 7, 12, 4, 1, 432)),
                ApparentWind(6.056565645616007, LocalDateTime.of(2017, 1, 7, 12, 4, 1, 1)))

        val result = testee.smooth(ApparentWind(7.022168445616007, LocalDateTime.of(2017, 1, 7, 12, 4, 2, 0)))

        assertThat(testee.cache).containsExactly(
                ApparentWind(7.1404906978132, LocalDateTime.of(2017, 1, 7, 12, 4, 1, 1)),
                ApparentWind(4.815201219850976, LocalDateTime.of(2017, 1, 7, 12, 4, 1, 232)),
                ApparentWind(5.453112492566276, LocalDateTime.of(2017, 1, 7, 12, 4, 1, 432)),
                ApparentWind(6.056565645616007, LocalDateTime.of(2017, 1, 7, 12, 4, 1, 1)),
                ApparentWind(7.022168445616007, LocalDateTime.of(2017, 1, 7, 12, 4, 2, 0)))

        assertThat(result.speed).isEqualTo(6.097507700292494)

    }

    @Test
    fun cacheWithOldValues_smooth_averageOfAllWindValuesWithinTheLastTwoSeconds() {
        val testee = ApparentWindSmoother()
        testee.cache = mutableListOf(
                ApparentWind(5.453112492566276, LocalDateTime.of(2017, 1, 7, 12, 4, 2, 0)),
                ApparentWind(6.056565645616007, LocalDateTime.of(2017, 1, 7, 12, 4, 3, 0))
        )

        val result = testee.smooth(ApparentWind(7.022168445616007, LocalDateTime.of(2017, 1, 7, 12, 4, 4, 0)))

        assertThat(result).isEqualTo(ApparentWind(6.177282194599431, LocalDateTime.of(2017, 1, 7, 12, 4, 4, 0)))
    }
}