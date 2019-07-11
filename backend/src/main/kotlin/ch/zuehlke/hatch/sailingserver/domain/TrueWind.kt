package ch.zuehlke.hatch.sailingserver.domain

import java.lang.Math.*

/***
 * @param direction is relative to north (not related to the boat's heading)
 * @param angle is relative to boat heading
 */
data class TrueWind(val speed: Double, val direction: Radiant, val angle: Radiant) {

    companion object {
        /***
         * Note: The formulas used here are based on StarpathTrueWind (@see https://www.starpath.com/freeware/true_wind.htm).
         * The formula is only accurate if the course over ground (COG) and magnetic heading (MH) are close to each other.
         *
         * However, if COG and MH differ significantly a more complex vector solution is needed (@see https://www.bwsailing.com/cc/2017/05/calculating-the-true-wind-and-why-it-matters/)
         * In the more complex case the magneticHeading is needed to calculate the true wind.
         */
        fun from(speedOverGround: Double, courseOverGround: Radiant, apparentWind: Wind, magneticHeading: Radiant): TrueWind {
            val trueWindSpeed = sqrt(pow(speedOverGround, 2.0) + pow(apparentWind.speed, 2.0) - (2 * speedOverGround * apparentWind.speed * cos(apparentWind.radiant.value)))
            val beta = (pow(apparentWind.speed, 2.0) - pow(trueWindSpeed, 2.0) - pow(speedOverGround, 2.0)) / (2 * trueWindSpeed * speedOverGround)
            val trueWindAngle = acos(beta)
            val trueWindDirection = courseOverGround.value + trueWindAngle
            return TrueWind(trueWindSpeed, Radiant(trueWindDirection), Radiant(trueWindAngle))
        }
    }
}
