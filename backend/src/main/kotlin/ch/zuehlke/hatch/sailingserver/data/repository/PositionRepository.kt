package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventStore
import ch.zuehlke.hatch.sailingserver.data.eventstore.PositionTransformer
import ch.zuehlke.hatch.sailingserver.domain.Position
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Repository
class PositionRepository constructor(private val eventStore: EventStore) {

    fun getPositions(from: LocalDateTime): Flux<Position> {
        return this.eventStore.find(from, PositionTransformer())
    }

    fun getPositions(from: LocalDateTime, to: LocalDateTime): Flux<Position> {
        return this.eventStore.find(from, to, PositionTransformer())
    }

}