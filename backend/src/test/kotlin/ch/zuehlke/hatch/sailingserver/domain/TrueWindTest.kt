package ch.zuehlke.hatch.sailingserver.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.Math.toRadians


internal class TrueWindTest {

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_1_heading_north() {
        val vesselSpeed = 4.4
        // north
        val vesselHeading = toRadians(0.0)
        val apparentWindSpeed = 7.0
        // starboard
        val apparentWindAngle = toRadians(20.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(3.236500356803323)
        // 48°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(0.8326711333131873))
        // 48°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(0.8326711333131873))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_1_heading_east() {
        val vesselSpeed = 4.4
        // east
        val vesselHeading = toRadians(90.0)
        val apparentWindSpeed = 7.0
        // starboard
        val apparentWindAngle = toRadians(20.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(3.236500356803323)
        // 48°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(0.8326711333131873))
        // 48° + 90° = 138°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(2.403467460108084))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_1_heading_south() {
        val vesselSpeed = 4.4
        // south
        val vesselHeading = toRadians(180.0)
        val apparentWindSpeed = 7.0
        // starboard
        val apparentWindAngle = toRadians(20.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(3.236500356803323)
        // 48°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(0.8326711333131873))
        // 48° + 180° = 228°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(3.9742637869029807))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_1_heading_west() {
        val vesselSpeed = 4.4
        // west
        val vesselHeading = toRadians(270.0)
        val apparentWindSpeed = 7.0
        // starboard
        val apparentWindAngle = toRadians(20.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(3.236500356803323)
        // 48°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(0.8326711333131873))
        // 48° + 270° = 318°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(5.545060113697877))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_2_heading_north() {
        val vesselSpeed = 4.4
        // north
        val vesselHeading = toRadians(10.0)
        val apparentWindSpeed = 7.0
        // starboard
        val apparentWindAngle = toRadians(110.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(9.456661188223949)
        // 136°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(2.372368134370689))
        // 146°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(2.546901059570122))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_2_heading_east() {
        val vesselSpeed = 4.4
        // east
        val vesselHeading = toRadians(100.0)
        val apparentWindSpeed = 7.0
        // starboard
        val apparentWindAngle = toRadians(110.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(9.456661188223949)
        // 136°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(2.372368134370689))
        // 146° + 90° = 236°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(4.117697386365019))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_2_heading_south() {
        val vesselSpeed = 4.4
        // south
        val vesselHeading = toRadians(190.0)
        val apparentWindSpeed = 7.0
        // starboard
        val apparentWindAngle = toRadians(110.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(9.456661188223949)
        // 136°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(2.372368134370689))
        // 146° + 180° = 326°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(5.688493713159915))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_2_heading_west() {
        val vesselSpeed = 4.4
        // west
        val vesselHeading = toRadians(280.0)
        val apparentWindSpeed = 7.0
        // starboard
        val apparentWindAngle = toRadians(110.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(9.456661188223949)
        // 136°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(2.372368134370689))
        // 146° + 90° = 416°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(7.259290039954811))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_3_heading_north() {
        val vesselSpeed = 4.4
        // north
        val vesselHeading = toRadians(10.0)
        val apparentWindSpeed = 2.0
        // input for starpath is 160° port, but we use 180° + 20° = 200° since we do not distinguish between starboard and port
        val apparentWindAngle = toRadians(200.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(6.316533078028801)
        // 174°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(3.0330862310551066))
        // 184°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(3.2076191562545393))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_3_heading_east() {
        val vesselSpeed = 4.4
        // east
        val vesselHeading = toRadians(75.0)
        val apparentWindSpeed = 2.0
        // input for starpath is 160° port, but we use 180° + 20° = 200° since we do not distinguish between starboard and port
        val apparentWindAngle = toRadians(200.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(6.316533078028801)
        // 174°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(3.0330862310551066))
        // 249°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(4.342083170050854))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_3_heading_south() {
        val vesselSpeed = 4.4
        // south
        val vesselHeading = toRadians(195.0)
        val apparentWindSpeed = 2.0
        // input for starpath is 160° port, but we use 180° + 20° = 200° since we do not distinguish between starboard and port
        val apparentWindAngle = toRadians(200.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(6.316533078028801)
        // 174°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(3.0330862310551066))
        // 369°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(6.436478272444049))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_3_heading_west() {
        val vesselSpeed = 4.4
        // west
        val vesselHeading = toRadians(275.0)
        val apparentWindSpeed = 2.0
        // input for starpath is 160° port, but we use 180° + 20° = 200° since we do not distinguish between starboard and port
        val apparentWindAngle = toRadians(200.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(6.316533078028801)
        // 174°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(3.0330862310551066))
        // 449°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(7.832741674039513))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_4_heading_east() {
        val vesselSpeed = 4.4
        // east
        val vesselHeading = toRadians(80.0)
        val apparentWindSpeed = 2.0
        // input for starpath is 60° port, but we use 270° + 30° = 300° since we do not distinguish between starboard and port
        val apparentWindAngle = toRadians(300.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(3.8157568056677826)
        // 153°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(2.6704321487405127))
        // 233°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(4.066695550335976))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_4_heading_south() {
        val vesselSpeed = 4.4
        // south
        val vesselHeading = toRadians(170.0)
        val apparentWindSpeed = 2.0
        // input for starpath is 60° port, but we use 270° + 30° = 300° since we do not distinguish between starboard and port
        val apparentWindAngle = toRadians(300.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(3.8157568056677826)
        // 153°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(2.6704321487405127))
        // 233°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(5.637491877130873))
    }

    @Test
    internal fun validInputs_construct_calculatedTrueWindAngle_case_quadrant_4_heading_west() {
        val vesselSpeed = 4.4
        // west
        val vesselHeading = toRadians(260.0)
        val apparentWindSpeed = 2.0
        // input for starpath is 60° port, but we use 270° + 30° = 300° since we do not distinguish between starboard and port
        val apparentWindAngle = toRadians(300.0)

        // Not used in simple case
        val irrelevant = Radiant(0.0)

        val trueWind = TrueWind.from(vesselSpeed, Radiant(vesselHeading), Wind(apparentWindSpeed, Radiant(apparentWindAngle)), irrelevant)

        Assertions.assertThat(trueWind.speed).isEqualTo(3.8157568056677826)
        // 153°
        Assertions.assertThat(trueWind.angle).isEqualTo(Radiant(2.6704321487405127))
        // 413°
        Assertions.assertThat(trueWind.direction).isEqualTo(Radiant(7.20828820392577))
    }
}


