package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.domain.Measurement
import ch.zuehlke.hatch.sailingserver.domain.MeasurementMessage

fun <T : Measurement> mapOrPropagateError(it: MeasurementMessage<T>, nestedFunc: (arg: T) -> T): MeasurementMessage<T> {
    return when (it) {
        is MeasurementMessage.Data -> MeasurementMessage.Data(nestedFunc(it.measurement))
        else -> it
    }
}