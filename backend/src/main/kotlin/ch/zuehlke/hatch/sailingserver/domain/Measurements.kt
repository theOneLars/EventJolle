package ch.zuehlke.hatch.sailingserver.domain

import java.time.temporal.ChronoUnit

abstract class Measurements(private vararg val measurements: Measurement) {

    init {
        if (measurements.isEmpty()) {
            throw RuntimeException("Measurements must not be empty")
        }
    }

    fun getNewest(): Measurement {
        return measurements
                .maxBy { it.timestamp }!!
    }

    fun getOldest(): Measurement {
        return measurements
                .minBy { it.timestamp }!!
    }

    fun getBiggestDelta(): Long {
        return ChronoUnit.MILLIS.between(getOldest().timestamp, getNewest().timestamp)
    }
}