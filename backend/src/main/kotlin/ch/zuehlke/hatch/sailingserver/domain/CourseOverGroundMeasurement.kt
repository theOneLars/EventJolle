package ch.zuehlke.hatch.sailingserver.domain

import java.time.LocalDateTime

data class CourseOverGroundMeasurement(val course: Radiant, val timestamp: LocalDateTime)