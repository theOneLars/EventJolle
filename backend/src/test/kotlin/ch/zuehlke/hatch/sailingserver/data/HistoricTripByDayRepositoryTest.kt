package ch.zuehlke.hatch.sailingserver.data

import ch.zuehlke.hatch.sailingserver.domain.ApparentWindMeasurement
import ch.zuehlke.hatch.sailingserver.domain.PositionMeasurement
import ch.zuehlke.hatch.sailingserver.domain.TripMeasurement
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import reactor.test.StepVerifier
import reactor.test.publisher.TestPublisher
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

internal class HistoricTripByDayRepositoryTest {

    private val from = LocalDateTime.of(
            LocalDate.now().minusDays(2),
            LocalTime.MIN)
    private val to = LocalDateTime.of(
            LocalDate.now(),
            LocalTime.MAX)

    @Test
    fun testGetTripsByDay() {
        // Setup the test case
        val positionRepository = mockk<CompressedPositionRepository>()
        val testee = HistoricTripByDayRepository(positionRepository)

        val compressedPositionPublisher = TestPublisher.create<PositionMeasurement>()
        every { positionRepository.getCompressedPositionStream(from, to) } returns compressedPositionPublisher.flux()

        val position1 = PositionMeasurement(LocalDateTime.now().minusDays(2), 2.5, 1.8)
        val position2 = PositionMeasurement(LocalDateTime.now().minusDays(2).plusHours(2), 2.8, 1.9)
        val position3 = PositionMeasurement(LocalDateTime.now().minusDays(1), 3.2, 2.0)
        val position4 = PositionMeasurement(LocalDateTime.now().minusDays(1).plusHours(3), 3.3, 2.1)
        val position5 = PositionMeasurement(LocalDateTime.now().minusHours(2), 3.4, 4.0)

        // Execute the test case and verify its results
        StepVerifier.create(testee.getTripsByDay(from.toLocalDate(), to.toLocalDate()))
                .then { compressedPositionPublisher.next(position1) }
                .then { compressedPositionPublisher.next(position2) }
                .then { compressedPositionPublisher.next(position3) }
                .then { compressedPositionPublisher.next(position4) }
                .then { compressedPositionPublisher.next(position5) }
                .then { compressedPositionPublisher.complete() }
                .expectNext(TripMeasurement(from, givenTripId(from), listOf(position1, position2)))
                .expectNext(TripMeasurement(from.plusDays(1), givenTripId(from.plusDays(1)), listOf(position3, position4)))
                .expectNext(TripMeasurement(from.plusDays(2), givenTripId(to), listOf(position5)))
                .verifyComplete()
    }

    private fun givenTripId(timestamp: LocalDateTime): String {
        return "Trip " + timestamp.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}