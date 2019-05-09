package ch.zuehlke.hatch.sailingserver.usecase.position

import ch.zuehlke.hatch.sailingserver.data.repository.PositionRepository
import ch.zuehlke.hatch.sailingserver.usecase.position.model.PositionDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Service
class PositionUseCase(val positionRepository: PositionRepository) {

    fun getPositionsOfLast10Years(): Flux<PositionDto> {
        val now = LocalDateTime.now()
        val tenYearsAgo = now.minusYears(10)
        return this.positionRepository.getHistoricPositions(tenYearsAgo, now)
                .map { PositionDto(it.timestamp, it.longitude, it.latitude) }
    }

}
