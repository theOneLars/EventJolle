package ch.zuehlke.hatch.sailingserver.usecase.cockpit

import ch.zuehlke.hatch.sailingserver.data.repository.ApparentWindRepository
import ch.zuehlke.hatch.sailingserver.data.repository.MagneticHeadingRepository
import ch.zuehlke.hatch.sailingserver.domain.ApparentWindMeasurement
import ch.zuehlke.hatch.sailingserver.domain.MagneticHeadingMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import ch.zuehlke.hatch.sailingserver.domain.Wind
import ch.zuehlke.hatch.sailingserver.processing.ApparentWindSmoother
import ch.zuehlke.hatch.sailingserver.usecase.cockpit.model.CockpitDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.test.publisher.TestPublisher
import java.time.LocalDateTime


class CockpitUseCaseTest {

    @Test
    internal fun multipleStream_getCockpit_combinedDto() {

        val apparentWindRepository = mockk<ApparentWindRepository>()
        val magneticHeadingRepository = mockk<MagneticHeadingRepository>()
        val apparentWindSmoother = mockk<ApparentWindSmoother>()

        val apparentWindPublisher = TestPublisher.create<ApparentWindMeasurement>()
        every { apparentWindRepository.getMockApparentWindStream() } returns apparentWindPublisher.flux()

        val magneticHeadingPublisher = TestPublisher.create<MagneticHeadingMeasurement>()
        every { magneticHeadingRepository.getMockMagneticHeadingStream() } returns magneticHeadingPublisher.flux()

        val capturedWind = slot<ApparentWindMeasurement>()
        every {
            apparentWindSmoother.smooth(capture(capturedWind))
        } answers {
            capturedWind.captured
        }

        val testee = CockpitUseCase(apparentWindRepository, magneticHeadingRepository, apparentWindSmoother)

        StepVerifier.create(testee.getCockpit())
                .then { apparentWindPublisher.next(ApparentWindMeasurement(Wind(7.1404906978132, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 0, 0))) }
                .then { magneticHeadingPublisher.next(MagneticHeadingMeasurement(Radiant(1.0), LocalDateTime.of(2017, 1, 7, 12, 4, 1, 0))) }
                .expectNext(CockpitDto(Wind(7.1404906978132, Radiant(1.0)), Wind(7.1404906978132, Radiant(1.0)), 1.0, Radiant(1.0), Radiant(1.0)))
                .then { apparentWindPublisher.next(ApparentWindMeasurement(Wind(1.462874378, Radiant(1.0)), LocalDateTime.of(2017, 1, 7, 12, 4, 2, 0))) }
                .expectNext(CockpitDto(Wind(1.462874378, Radiant(1.0)), Wind(1.462874378, Radiant(1.0)), 1.0, Radiant(1.0), Radiant(1.0)))
                .then { magneticHeadingPublisher.next(MagneticHeadingMeasurement(Radiant(2.5), LocalDateTime.of(2017, 1, 7, 12, 4, 3, 0))) }
                .expectNext(CockpitDto(Wind(1.462874378, Radiant(1.0)), Wind(1.462874378, Radiant(1.0)), 1.0, Radiant(1.0), Radiant(2.5)))
                .then { magneticHeadingPublisher.complete() }
                .then { apparentWindPublisher.complete() }
                .verifyComplete()
    }
}