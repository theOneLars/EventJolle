package ch.zuehlke.hatch.sailingserver.usecase.cockpit

import ch.zuehlke.hatch.sailingserver.data.repository.ApparentWindRepository
import ch.zuehlke.hatch.sailingserver.data.repository.CourseOverGroundRepository
import ch.zuehlke.hatch.sailingserver.data.repository.MagneticHeadingRepository
import ch.zuehlke.hatch.sailingserver.data.repository.SpeedOverGroundRepository
import ch.zuehlke.hatch.sailingserver.domain.*
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
        val courseOverGroundRepository = mockk<CourseOverGroundRepository>()
        val speedOverGroundRepository = mockk<SpeedOverGroundRepository>()
        val apparentWindSmoother = mockk<ApparentWindSmoother>()

        val apparentWindPublisher = TestPublisher.create<ApparentWindMeasurement>()
        every { apparentWindRepository.getMockApparentWindStream() } returns apparentWindPublisher.flux()

        val magneticHeadingPublisher = TestPublisher.create<MagneticHeadingMeasurement>()
        every { magneticHeadingRepository.getMockMagneticHeadingStream() } returns magneticHeadingPublisher.flux()

        val courseOverGroundPublisher = TestPublisher.create<CourseOverGroundMeasurement>()
        every { courseOverGroundRepository.getMockCourseOverGround() } returns courseOverGroundPublisher.flux()

        val speedOverGroundPublisher = TestPublisher.create<SpeedOverGroundMeasurement>()
        every { speedOverGroundRepository.getMockSpeedOverGround() } returns speedOverGroundPublisher.flux()

        val capturedWind = slot<ApparentWindMeasurement>()
        every {
            apparentWindSmoother.smooth(capture(capturedWind))
        } answers {
            capturedWind.captured
        }

        val testee = CockpitUseCase(apparentWindRepository, magneticHeadingRepository, speedOverGroundRepository, courseOverGroundRepository, apparentWindSmoother)

        StepVerifier.create(testee.getCockpit())
                .then { apparentWindPublisher.next(ApparentWindMeasurement(Wind(7.1404906978132, Radiant(1.0)), getTimestampWithDelay(0))) }
                .then { magneticHeadingPublisher.next(MagneticHeadingMeasurement(Radiant(1.0), getTimestampWithDelay(1))) }
                .then { courseOverGroundPublisher.next(CourseOverGroundMeasurement(Radiant(2.3), getTimestampWithDelay(1))) }
                .then { speedOverGroundPublisher.next(SpeedOverGroundMeasurement(4.35, getTimestampWithDelay(1))) }
                .expectNext(CockpitDto(Wind(7.1404906978132, Radiant(1.0)), Wind(7.1404906978132, Radiant(1.0)), 4.35, Radiant(2.3), Radiant(1.0)))

                .then { apparentWindPublisher.next(ApparentWindMeasurement(Wind(1.462874378, Radiant(1.0)), getTimestampWithDelay(2))) }
                .expectNext(CockpitDto(Wind(1.462874378, Radiant(1.0)), Wind(1.462874378, Radiant(1.0)), 4.35, Radiant(2.3), Radiant(1.0)))

                .then { magneticHeadingPublisher.next(MagneticHeadingMeasurement(Radiant(2.5), getTimestampWithDelay(3))) }
                .expectNext(CockpitDto(Wind(1.462874378, Radiant(1.0)), Wind(1.462874378, Radiant(1.0)), 4.35, Radiant(2.3), Radiant(2.5)))

                .then { magneticHeadingPublisher.complete() }
                .then { apparentWindPublisher.complete() }
                .then { courseOverGroundPublisher.complete() }
                .then { speedOverGroundPublisher.complete() }
                .verifyComplete()
    }

    private fun getTimestampWithDelay(secondsDelay: Int = 0, minutesDelay: Int = 4): LocalDateTime {
        return LocalDateTime.of(2017, 1, 7, 12, minutesDelay, secondsDelay, 0)
    }

}