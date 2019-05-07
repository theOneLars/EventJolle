package ch.zuehlke.hatch.sailingserver.usecase.cockpit.model

import ch.zuehlke.hatch.sailingserver.domain.Radiant
import ch.zuehlke.hatch.sailingserver.domain.Wind

data class CockpitDto(
        val apparentWind: Wind,
        val trueWind: Wind,
        val speedOverGround: Double,
        val courseOverGround: Radiant,
        val magneticHeading: Radiant)