package ch.zuehlke.hatch.sailingserver.livecache

import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Flux
import java.time.Duration

class LiveCache<V, T : TemporalIdentifier<T>>(
        liveUpdateStream: Flux<V>,
        private val selector: (V) -> T
) {
    // singleton, guarantee the last 30 seconds are in the cache
    val updatePublisher: ConnectableFlux<V>

    init {
        // singleton
        this.updatePublisher = liveUpdateStream.replay(Duration.ofSeconds(30))
        this.updatePublisher.connect()
    }

    // per request
    fun withSnapshot(snapshotStream: Flux<V>): Flux<V> {
        return snapshotStream.concatWith(updatePublisher).distinct(
                { value -> this.selector(value) },
                { ReadPointer<T>() },
                { pointer, id -> pointer.read(id) },
                { c -> c.reset() }
        )
    }


}
