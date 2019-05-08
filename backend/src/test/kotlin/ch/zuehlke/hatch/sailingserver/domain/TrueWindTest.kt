package ch.zuehlke.hatch.sailingserver.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TrueWindTest {


    @Test
    internal fun validInputs_construct_calculatedTrueWind() {

        val result = TrueWind.from(3.0, Radiant(2.3), Wind(20.0, Radiant(1.3)))

        assertThat(result.wind.radiant).isEqualTo(Radiant(1.1635016797303188))
        assertThat(result.wind.speed).isEqualTo(18.551650150210985)
    }
}


