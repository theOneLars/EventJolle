package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.domain.PositionMeasurement
import ch.zuehlke.hatch.sailingserver.domain.TripMeasurement
import ch.zuehlke.hatch.sailingserver.processing.TripDetector
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.LocalTime

@Repository
class HistoricTripByDayRepository(
        private val positionRepository: CompressedPositionRepository
) {

    fun getTripsByDay(from: LocalDate, to: LocalDate): Flux<TripMeasurement> {
        val tripDetector = TripDetector()

        val fromTimestamp = from.atStartOfDay()
        val toTimestamp = to.atTime(LocalTime.MAX)

        return this.positionRepository.getCompressedPositionStream(fromTimestamp, toTimestamp)
                .groupBy { position -> tripDetector.getIdentifierFor(position) }
                .flatMap { group -> toTripMeasurement(group.key()!!, group) }
                .sort(TripMeasurement::compareTo)
    }

    private fun toTripMeasurement(tripId: String, positions: Flux<PositionMeasurement>): Mono<TripMeasurement> {
        return positions
                .collectList()
                .map { positionList -> createTripMeasurement(tripId, positionList) }
    }

    private fun createTripMeasurement(tripId: String, positions: List<PositionMeasurement>): TripMeasurement {
        val sortedPositions = positions.sortedBy { position -> position.timestamp }
        val timestamp = sortedPositions.first().getTimestampDate().atStartOfDay()

        return TripMeasurement(timestamp, tripId, sortedPositions)
    }
}