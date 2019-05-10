package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventStore
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindAngleMeasurement
import ch.zuehlke.hatch.sailingserver.livecache.LiveCache
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Repository
class ApparentWindAngleRepository(
        private val eventStore: EventStore,
        private val liveUpdateRepository: LiveUpdateRepository
) {

    private val liveCache: LiveCache<ApparentWindAngleMeasurement, TimeBasedIdentifier>

    init {
        val liveStream = this.liveUpdateRepository.getLiveStream(ApparentWindAngleTransformer())
        this.liveCache = LiveCache(liveStream) { apparentWindAngle -> TimeBasedIdentifier(apparentWindAngle.timestamp) }
    }

    fun getApparentWindAngles(): Flux<ApparentWindAngleMeasurement> {
        return this.liveUpdateRepository.getLiveStream(ApparentWindAngleTransformer())
    }

    fun getApparentWindAngles(from: LocalDateTime): Flux<ApparentWindAngleMeasurement> {
        val snapshotStream = this.eventStore.find(from, ApparentWindAngleTransformer())

        return this.liveCache.withSnapshot(snapshotStream)
    }

    fun getHistoricApparentWindAngles(from: LocalDateTime, to: LocalDateTime): Flux<ApparentWindAngleMeasurement> {
        return this.eventStore.find(from, to, ApparentWindAngleTransformer())
    }
}