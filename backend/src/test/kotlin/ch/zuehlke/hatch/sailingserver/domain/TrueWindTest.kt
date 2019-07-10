//package ch.zuehlke.hatch.sailingserver.domain
//
//import jdk.nashorn.internal.ir.annotations.Ignore
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Test
//import java.lang.Math.toRadians
//import kotlin.math.PI
//
//
//internal class TrueWindTest {
//
//
//    @Test
//    @Ignore
//    internal fun validInputs_construct_calculatedTrueWind() {
//
//        val result = TrueWind.from(3.0, Radiant(2.3), Wind(20.0, Radiant(1.3)), Radiant(4.3))
//
//        assertThat(result.direction).isEqualTo(Radiant(1.1635016797303188))
//        assertThat(result.angle).isEqualTo(Radiant(3.136498320269681))
//        assertThat(result.speed).isEqualTo(18.551650150210985)
//    }
//
//    @Test
//    @Ignore
//    internal fun validInputs_construct_calculatedTrueWindAngle() {
//
//        val result = TrueWind.from(3.0, Radiant(2.3), Wind(20.0, Radiant(1.3)), Radiant(5.0))
//
//        assertThat(result.direction).isEqualTo(Radiant(1.1635016797303188))
//        assertThat(result.angle).isEqualTo(Radiant(2.446686986909905))
//        assertThat(result.speed).isEqualTo(18.551650150210985)
//    }
//
//    @Test
//    @Ignore
//    internal fun validInputs_construct_calculatedTrueWindAngle_case_gegen_wind_links_vom_boot() {
//        val heading = toRadians(90.0)
//        val sog = 2.0
//        val cog = PI / 2 // 90°
//        val awa = toRadians(90.0 + 45)
//        val aws = 4.0
//
//        val result = TrueWind.from(sog, Radiant(cog), Wind(aws, Radiant(awa)), Radiant(heading))
//
//        assertThat(result.direction).isEqualTo(Radiant(-0.2849241266220623))
//        assertThat(result.angle).isEqualTo(Radiant(1.855720453416959))
//        assertThat(result.speed).isEqualTo(2.9472515164158013)
//    }
//
//    @Test
//    @Ignore
//    internal fun validInputs_construct_calculatedTrueWindAngle_gegen_wind_rechts_vom_boot() {
//        val heading = toRadians(90.0)
//        val sog = 2.0
//        val cog = PI / 2 // 90°
//        val awa = toRadians(180.0 + 45)
//        val aws = 4.0
//
//        val result = TrueWind.from(sog, Radiant(cog), Wind(aws, Radiant(awa)), Radiant(heading))
//
//        assertThat(result.direction).isEqualTo(Radiant(-2.8566685269677308))
//        assertThat(result.angle).isEqualTo(Radiant(1.855720453416959))
//        assertThat(result.speed).isEqualTo(2.947251516415801)
//    }
//
//
//    @Test
//    @Ignore
//    internal fun validInputs_construct_calculatedTrueWindAngle_case_mit_wind_links_vom_boot() {
//        val heading = toRadians(90.0)
//        val sog = 2.0
//        val cog = PI / 2 // 90°
//        val awa = toRadians(90.0 - 20)
//        val aws = 3.0
//
//        val result = TrueWind.from(sog, Radiant(cog), Wind(aws, Radiant(awa)), Radiant(heading))
//
//        assertThat(result.direction).isEqualTo(Radiant(0.8617822283177117))
//        assertThat(result.angle).isEqualTo(Radiant(0.7500018891792563))
//        assertThat(result.speed).isEqualTo(4.135727471667836)
//    }
//
//    @Test
//    internal fun validInputs_construct_calculatedTrueWindAngle_case_mit_wind_rechts_vom_boot() {
//        val heading = toRadians(90.0)
//        val sog = 4.0
//        val cog = PI / 2 // 90°
//        val awa = toRadians(290.0)
//        val aws = 4.0
//
//        val result = TrueWind.from(sog, Radiant(cog), Wind(aws, Radiant(awa)), Radiant(heading))
//
//        assertThat(result.direction).isEqualTo(Radiant(2.391590764))
//        assertThat(result.angle).isEqualTo(Radiant(1.855720453416959))
//        assertThat(result.speed).isEqualTo(2.9472515164158013)
//    }
//}
//
//
