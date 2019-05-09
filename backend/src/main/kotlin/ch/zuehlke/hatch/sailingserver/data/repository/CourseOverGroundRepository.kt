package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.StreamHealthProcessor
import ch.zuehlke.hatch.sailingserver.data.eventstore.EventStore
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindAngleMeasurement
import ch.zuehlke.hatch.sailingserver.domain.CourseOverGroundMeasurement
import ch.zuehlke.hatch.sailingserver.domain.MeasurementMessage
import ch.zuehlke.hatch.sailingserver.livecache.LiveCache
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Repository
class CourseOverGroundRepository(
        private val eventStore: EventStore,
        private val liveUpdateRepository: LiveUpdateRepository
) {

    private val liveCache: LiveCache<CourseOverGroundMeasurement, TimeBasedIdentifier>
    private val streamHealthProcessor = StreamHealthProcessor<CourseOverGroundMeasurement>()

    init {
        val liveStream = this.liveUpdateRepository.getLiveStream(CourseOverGroundTransformer())
        this.liveCache = LiveCache(liveStream) { heading -> TimeBasedIdentifier(heading.timestamp) }
    }

    fun getCourseOverGround(): Flux<MeasurementMessage<CourseOverGroundMeasurement>> {
        return streamHealthProcessor.process(this.liveUpdateRepository.getLiveStream(CourseOverGroundTransformer()))
    }

    fun getCourseOverGround(from: LocalDateTime): Flux<MeasurementMessage<CourseOverGroundMeasurement>> {
        val find = this.eventStore.find(from, CourseOverGroundTransformer())
        return streamHealthProcessor.process(this.liveCache.withSnapshot(find))
    }

    fun getHistoricCourseOverGround(from: LocalDateTime, to: LocalDateTime): Flux<MeasurementMessage<CourseOverGroundMeasurement>>{
        return streamHealthProcessor.process(this.eventStore.find(from, to, CourseOverGroundTransformer()))
    }

}