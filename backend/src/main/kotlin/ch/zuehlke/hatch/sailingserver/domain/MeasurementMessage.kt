package ch.zuehlke.hatch.sailingserver.domain

//class MeasurementMessage<out T: Measurement>(val measurement: T?, val measurementState: MeasurementState)

sealed class MeasurementMessage<out T : Measurement> {
    data class Data<out T: Measurement>(val measurement: T) : MeasurementMessage<T>()
    data class Failure<out T: Measurement>(val reason: String) : MeasurementMessage<T>()
}