package ch.zuehlke.hatch.sailingserver.usecase.cockpit.model

import ch.zuehlke.hatch.sailingserver.domain.Radiant
import ch.zuehlke.hatch.sailingserver.domain.TrueWind
import ch.zuehlke.hatch.sailingserver.domain.Wind

data class CockpitDto(
        val apparentWind: Wind,
        val trueWind: TrueWind,
        val speedOverGround: Double,
        val courseOverGround: Radiant,
        val magneticHeading: Radiant,
        val velocityMadeGood: Double)
