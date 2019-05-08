package ch.zuehlke.hatch.sailingserver.domain

data class TrueWind(val wind: Wind) {

    companion object {
        fun from(speedOverGround: Double, courseOverGround: Radiant, apparentWind: Wind): TrueWind {
            val u = speedOverGround * Math.sin(courseOverGround.value) - apparentWind.speed * Math.sin(apparentWind.radiant.value)
            val v = speedOverGround * Math.cos(courseOverGround.value) - apparentWind.speed * Math.cos(apparentWind.radiant.value)

            val trueWindSpeed = Math.sqrt(u * u + v * v)
            val trueWindDirection = Math.atan(u / v)

            return TrueWind(Wind(trueWindSpeed, Radiant(trueWindDirection)))
        }
    }
}
