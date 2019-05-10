package ch.zuehlke.hatch.sailingserver.usecase.position

import ch.zuehlke.hatch.sailingserver.data.HistoricTripByDayRepository
import ch.zuehlke.hatch.sailingserver.domain.PositionMeasurement
import ch.zuehlke.hatch.sailingserver.domain.TripMeasurement
import ch.zuehlke.hatch.sailingserver.usecase.position.model.PositionDto
import ch.zuehlke.hatch.sailingserver.usecase.position.model.TripDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.LocalDate

@Service
class ViewHistoricTrips(
        private val historicTripByDayRepository: HistoricTripByDayRepository
) {

    fun tillToday(from: LocalDate): Flux<TripDto> {
        return ofRange(from, LocalDate.now())
    }

    fun ofRange(from: LocalDate, to: LocalDate): Flux<TripDto> {
        return this.historicTripByDayRepository.getTripsByDay(from, to)
                .map { trip -> toTripDto(trip) }
    }

    private fun toTripDto(trip: TripMeasurement): TripDto {
        return TripDto(
                trip.timestamp.toLocalDate(),
                trip.id,
                trip.positions.map { toPositionDto(it) }
        )
    }

    private fun toPositionDto(position: PositionMeasurement): PositionDto {
        return PositionDto(position.timestamp, position.longitude, position.latitude)
    }
}