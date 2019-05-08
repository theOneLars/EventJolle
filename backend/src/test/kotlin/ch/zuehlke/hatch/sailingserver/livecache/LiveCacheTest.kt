package ch.zuehlke.hatch.sailingserver.livecache

import ch.zuehlke.hatch.sailingserver.data.repository.TimeBasedIdentifier
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration
import java.time.LocalDateTime

internal class LiveCacheTest {

    @Test
    fun testCache() {

        // C, D, E
        val liveData = Flux.interval(Duration.ofSeconds(1)).map { (67+it).toChar() }.take(3)
        val now = LocalDateTime.now()
        val liveCache = LiveCache(liveData) { char -> TimeBasedIdentifier(now.plusDays(char.toLong())) }

        val repoStream = Flux.just('A', 'B', 'C')
        val liveStream = liveCache.withSnapshot(repoStream)
        StepVerifier.create(liveStream.log())
                .expectNext('A')
                .expectNext('B')
                .expectNext('C')
                .expectNext('D')
                .expectNext('E')
                .verifyComplete()

        val secondRepoStream = Flux.just('A', 'B', 'C', 'D')
        val secondLiveStream = liveCache.withSnapshot(secondRepoStream)
        StepVerifier.create(secondLiveStream.log())
                .expectNext('A')
                .expectNext('B')
                .expectNext('C')
                .expectNext('D')
                .expectNext('E')
                .verifyComplete()


        val thirdRepoStream = Flux.just('A','B')
        val thirdLiveStream = liveCache.withSnapshot(thirdRepoStream)
        StepVerifier.create(thirdLiveStream.log())
                .expectNext('A')
                .expectNext('B')
                .expectNext('C')
                .expectNext('D')
                .expectNext('E')
                .verifyComplete()
    }

    @Test
    fun testCacheWithObjects() {

        // C, D, E
        val liveData = Flux.interval(Duration.ofSeconds(1)).map { Pair(it, (67+it).toChar()) }.take(3)

        val now = LocalDateTime.now()
        val liveCache = LiveCache(liveData, { pair -> TimeBasedIdentifier(now.plusDays(pair.first)) })

        val repoStream = Flux.just(Pair(-2L,'A'), Pair(-1L,'B'), Pair(0L,'C'))
        val liveStream = liveCache.withSnapshot(repoStream)
        StepVerifier.create(liveStream.log())
                .expectNext(Pair(-2L,'A'))
                .expectNext(Pair(-1L,'B'))
                .expectNext(Pair(0L,'C'))
                .expectNext(Pair(1L,'D'))
                .expectNext(Pair(2L,'E'))
                .verifyComplete()

    }
}