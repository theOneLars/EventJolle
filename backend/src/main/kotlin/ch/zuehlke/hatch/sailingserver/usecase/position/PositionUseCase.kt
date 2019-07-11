package ch.zuehlke.hatch.sailingserver.usecase.position

import ch.zuehlke.hatch.sailingserver.data.CompressedPositionRepository
import ch.zuehlke.hatch.sailingserver.usecase.position.model.PositionDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class PositionUseCase(val positionRepository: CompressedPositionRepository) {

    fun getPositionsOfLast10Years(): Flux<PositionDto> {
        val now = LocalDateTime.now()
        val tenYearsAgo = now.minusYears(10)

        return this.positionRepository.getCompressedPositionStream(tenYearsAgo, now)
                .map { PositionDto(it.timestamp, it.longitude, it.latitude) }
    }

    fun getTodayLivePositions(): Flux<PositionDto> {
        return positionRepository.getCompressedTodayLivePositionStream()
                .map { PositionDto(it.timestamp, it.longitude, it.latitude) }
    }

    fun getPositionsOfDate(date: LocalDate): Flux<PositionDto> {
        val from = date.atStartOfDay()
        val to = date.atTime(23, 59, 59, 999)

        return this.positionRepository.getCompressedPositionStream(from, to)
                .map { PositionDto(it.timestamp, it.longitude, it.latitude) }
    }
}
