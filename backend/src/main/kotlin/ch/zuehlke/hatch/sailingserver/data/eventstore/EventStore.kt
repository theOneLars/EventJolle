package ch.zuehlke.hatch.sailingserver.data.eventstore

import com.google.gson.JsonObject
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import java.time.LocalDateTime

interface EventStore {
    fun <T> find(from: LocalDateTime, transformer: EventTransformer<T>) : Flux<T>
    fun <T> find(from: LocalDateTime, to: LocalDateTime, transformer: EventTransformer<T>) : Flux<T>
    fun insert(document: JsonObject): Publisher<Success>
}