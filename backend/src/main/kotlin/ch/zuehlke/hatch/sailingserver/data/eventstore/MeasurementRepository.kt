package ch.zuehlke.hatch.sailingserver.data.eventstore

import org.springframework.data.r2dbc.repository.query.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

interface MeasurementRepository : ReactiveCrudRepository<Measurement, Long> {

    @Query("SELECT id, path, timestamp, measurement FROM measurement m WHERE m.path = :path ORDER BY timestamp ASC")
    fun findByPath(path: String): Flux<Measurement>

    @Query("SELECT id, path, timestamp, measurement FROM measurement m WHERE m.path = :path AND m.timestamp >= cast(:timestamp as timestamp) ORDER BY timestamp ASC")
    fun findByPathAndTimestampIsGreaterThanEqual(path: String, timestamp: LocalDateTime): Flux<Measurement>

    @Query("SELECT id, path, timestamp, measurement FROM measurement m WHERE m.path = :path AND m.timestamp >= cast(:from as timestamp) AND m.timestamp < cast(:to as timestamp) ORDER BY timestamp ASC")
    fun findByPathAndTimestampIsBetween(path: String, from: LocalDateTime, to: LocalDateTime): Flux<Measurement>
}