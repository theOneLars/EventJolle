package ch.zuehlke.hatch.sailingserver.domain

import kotlin.math.*

/***
 * @param direction is relative to north (not related to the boat's heading)
 * @param angle is relative to boat heading
 */
data class TrueWind(val speed: Double, val direction: Radiant, val angle: Radiant) {

    companion object {
        fun from(speedOverGround: Double, courseOverGround: Radiant, apparentWind: Wind, heading: Radiant): TrueWind {
            var apparentWindAngle = apparentWind.radiant.value
            val apparentWindDirection = heading.value - apparentWindAngle

            var cog = courseOverGround.value
            if (apparentWindAngle > PI / 2 && apparentWindAngle < 1.5 * PI) {
                cog = cog - PI
            }

            val u = speedOverGround * sin(cog) - apparentWind.speed * sin(apparentWindDirection)
            val v = speedOverGround * cos(cog) - apparentWind.speed * cos(apparentWindDirection)

            val trueWindSpeed = sqrt(u * u + v * v)
            var trueWindDirection = atan(u / v)
            if (apparentWindAngle > PI) {
                trueWindDirection = trueWindDirection - PI
            }
            var trueWindAngle = abs(trueWindDirection - heading.value)
            if (trueWindAngle > Math.PI) {
                trueWindAngle = 2 * Math.PI - trueWindAngle
            }

            return TrueWind(trueWindSpeed, Radiant(trueWindDirection), Radiant(trueWindAngle))
        }
    }
}
