package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.data.repository.PositionRepository
import ch.zuehlke.hatch.sailingserver.domain.PositionMeasurement
import ch.zuehlke.hatch.sailingserver.processing.PositionFilter
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class CompressedPositionRepository(
        private val positionRepository: PositionRepository
) {

    fun getCompressedPositionStream(from: LocalDateTime, to: LocalDateTime): Flux<PositionMeasurement> {
        val filter = PositionFilter(Duration.ofSeconds(60))
        return this.positionRepository.getHistoricPositions(from = from, to = to)
                .filter { filter.accept(it) }
    }

    fun getCompressedTodayLivePositionStream(): Flux<PositionMeasurement> {
        val filter = PositionFilter(Duration.ofSeconds(60))

        return this.positionRepository.getPositions(LocalDate.now().atStartOfDay())
                .filter { filter.accept(it) }
    }
}