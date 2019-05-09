package ch.zuehlke.hatch.sailingserver.livecache

import reactor.core.publisher.Flux
import reactor.core.publisher.ReplayProcessor
import java.time.Duration

class LiveCache<V, T : TemporalIdentifier<T>>(
        liveUpdateStream: Flux<V>,
        private val selector: (V) -> T
) {
    // singleton, guarantee the last 30 seconds are in the cache
    private var replayProcessor: ReplayProcessor<V> = ReplayProcessor.createTimeout<V>(Duration.ofSeconds(30))

    init {
        // singleton
        liveUpdateStream.subscribe { value -> this.replayProcessor.onNext(value) }
    }

    // per request
    fun withSnapshot(snapshotStream: Flux<V>): Flux<V> {
        return snapshotStream
                .concatWith(replayProcessor)
                .distinct(
                        { value -> this.selector(value) },
                        { ReadPointer<T>() },
                        { pointer, id -> pointer.read(id) },
                        { c -> c.reset() }

                )
    }
}
