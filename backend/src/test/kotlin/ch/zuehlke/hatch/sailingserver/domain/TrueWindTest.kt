package ch.zuehlke.hatch.sailingserver.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TrueWindTest {


    @Test
    internal fun validInputs_construct_calculatedTrueWind() {

        val result = TrueWind.from(3.0, Radiant(2.3), Wind(20.0, Radiant(1.3)), Radiant(4.3))

        assertThat(result.direction).isEqualTo(Radiant(1.1635016797303188))
        assertThat(result.angle).isEqualTo(Radiant(3.136498320269681))
        assertThat(result.speed).isEqualTo(18.551650150210985)
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle() {

        val result = TrueWind.from(3.0, Radiant(2.3), Wind(20.0, Radiant(1.3)), Radiant(5.0))

        assertThat(result.direction).isEqualTo(Radiant(1.1635016797303188))
        assertThat(result.angle).isEqualTo(Radiant(2.446686986909905))
        assertThat(result.speed).isEqualTo(18.551650150210985)
    }

}


