package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.domain.Measurement
import ch.zuehlke.hatch.sailingserver.domain.MeasurementMessage
import reactor.core.publisher.Flux

class StreamHealthProcessor<T : Measurement> {
    //There will be health checks based on state/cached previous messages. This is why fun process() is not static

    fun process(input: Flux<T>): Flux<MeasurementMessage<T>> {
        return input.map { measurement -> MeasurementMessage.Data(measurement) }
    }
}