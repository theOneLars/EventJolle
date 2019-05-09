package ch.zuehlke.hatch.sailingserver.usecase.sample

import ch.zuehlke.hatch.sailingserver.data.repository.ApparentWindAngleRepository
import ch.zuehlke.hatch.sailingserver.data.repository.ApparentWindSpeedRepository
import ch.zuehlke.hatch.sailingserver.data.repository.PositionRepository
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindAngleMeasurement
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindSpeedMeasurement
import ch.zuehlke.hatch.sailingserver.domain.MeasurementMessage
import ch.zuehlke.hatch.sailingserver.domain.PositionMeasurement
import ch.zuehlke.hatch.sailingserver.signalk.SignalKService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDateTime

@RestController
@RequestMapping("/sample")
class SampleController(signalKService: SignalKService,
                       private val apparentWindAngleRepository: ApparentWindAngleRepository,
                       private val apparentWindSpeedRepository: ApparentWindSpeedRepository,
                       private val positionRepository: PositionRepository
) {

    init {
        signalKService.getFullServerInfo()
    }

    private val sample: Mono<String>
        @GetMapping
        get() = Mono.just("sample string")

    private val sampleFlux: Flux<String>
        @GetMapping(path = ["/flux"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
        get() = Flux
                .interval(Duration.ofMillis(2000))
                .map { tick -> "string " + tick!! }

    @GetMapping(path = ["/position/history"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getPositions(@RequestParam("from")
                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                     from: LocalDateTime,
                     @RequestParam("to")
                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                     to: LocalDateTime): Flux<PositionMeasurement> {
        return this.positionRepository.getHistoricPositions(from, to)
    }

    @GetMapping(path = ["/position/historyAndLive"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getHistoryAndLivePositions(@RequestParam("from")
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                   from: LocalDateTime): Flux<PositionMeasurement> {
        return this.positionRepository.getPositions(from)
    }

    @GetMapping(path = ["/position/live"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getLivePositions(): Flux<PositionMeasurement> {
        return this.positionRepository.getPositions()
    }

    @GetMapping(path = ["/apparentWindAngle/history"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getApparentWindAngle(@RequestParam("from")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                             from: LocalDateTime,
                             @RequestParam("to")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                             to: LocalDateTime): Flux<ApparentWindAngleMeasurement> {
        return this.apparentWindAngleRepository.getHistoricApparentWindAngles(from, to)
                .map { message ->
                    when (message) {
                        is MeasurementMessage.Data -> message.measurement
                        else -> throw RuntimeException("error in data")
                    }
                }
    }

    @GetMapping(path = ["/apparentWindAngle/historyAndLive"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getHistoryAndLiveApparentWindAngle(@RequestParam("from")
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                           from: LocalDateTime): Flux<ApparentWindAngleMeasurement> {
        return this.apparentWindAngleRepository.getApparentWindAngles(from)
                .map { message ->
                    when (message) {
                        is MeasurementMessage.Data -> message.measurement
                        else -> throw RuntimeException("error in data")
                    }
                }
    }

    @GetMapping(path = ["/apparentWindAngle/live"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getLiveApparentWindAngle(): Flux<ApparentWindAngleMeasurement> {
        return this.apparentWindAngleRepository.getApparentWindAngles()
                .map { message ->
                    when (message) {
                        is MeasurementMessage.Data -> message.measurement
                        else -> throw RuntimeException("error in data")
                    }
                }
    }

    @GetMapping(path = ["/apparentWindSpeed/history"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getApparentWindSpeed(@RequestParam("from")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                             from: LocalDateTime,
                             @RequestParam("to")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                             to: LocalDateTime): Flux<ApparentWindSpeedMeasurement> {
        return this.apparentWindSpeedRepository.getHistoricApparentWindSpeeds(from, to)
                .map { message ->
                    when (message) {
                        is MeasurementMessage.Data -> message.measurement
                        else -> throw RuntimeException("error in data")
                    }
                }
    }

    @GetMapping(path = ["/apparentWindSpeed/historyAndLive"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getHistoryAndLiveApparentWindSpeed(@RequestParam("from")
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                           from: LocalDateTime): Flux<ApparentWindSpeedMeasurement> {
        return this.apparentWindSpeedRepository.getApparentWindSpeeds(from)
                .map { message ->
                    when (message) {
                        is MeasurementMessage.Data -> message.measurement
                        else -> throw RuntimeException("error in data")
                    }
                }
    }

    @GetMapping(path = ["/apparentWindSpeed/live"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getLiveApparentWindSpeed(): Flux<ApparentWindSpeedMeasurement> {
        return this.apparentWindSpeedRepository.getApparentWindSpeeds()
                .map { message ->
                    when (message) {
                        is MeasurementMessage.Data -> message.measurement
                        else -> throw RuntimeException("error in data")
                    }
                }
    }
}

