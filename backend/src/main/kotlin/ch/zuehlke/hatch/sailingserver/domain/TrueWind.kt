package ch.zuehlke.hatch.sailingserver.domain

import kotlin.math.*

/***
 * @param direction is relative to north (not related to the boat's heading)
 * @param angle is relative to boat heading
 */
data class TrueWind(val speed: Double, val direction: Radiant, val angle: Radiant) {

    companion object {
        fun from(speedOverGround: Double, courseOverGround: Radiant, apparentWind: Wind, heading: Radiant): TrueWind {
            val u = speedOverGround * sin(courseOverGround.value) - apparentWind.speed * sin(apparentWind.radiant.value)
            val v = speedOverGround * cos(courseOverGround.value) - apparentWind.speed * cos(apparentWind.radiant.value)

            val trueWindSpeed = sqrt(u * u + v * v)
            val trueWindDirection = atan(u / v)
            var trueWindAngle = abs(trueWindDirection - heading.value)
            if (trueWindAngle > Math.PI) {
                trueWindAngle = 2 * Math.PI - trueWindAngle
            }

            return TrueWind(trueWindSpeed, Radiant(trueWindDirection), Radiant(trueWindAngle))
        }
    }
}
