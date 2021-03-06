package ch.zuehlke.hatch.sailingserver.usecase.cockpit

import ch.zuehlke.hatch.sailingserver.data.SmoothedApparentWindRepository
import ch.zuehlke.hatch.sailingserver.data.TrueWindRepository
import ch.zuehlke.hatch.sailingserver.data.VelocityMadeGoodRepository
import ch.zuehlke.hatch.sailingserver.data.repository.CourseOverGroundRepository
import ch.zuehlke.hatch.sailingserver.data.repository.MagneticHeadingRepository
import ch.zuehlke.hatch.sailingserver.data.repository.SpeedOverGroundRepository
import ch.zuehlke.hatch.sailingserver.domain.*
import ch.zuehlke.hatch.sailingserver.usecase.cockpit.model.CockpitDto
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.test.publisher.TestPublisher
import java.time.LocalDateTime


class CockpitUseCaseTest {

    @Test
    internal fun multipleStream_getCockpit_combinedDto() {

        val smoothApparentWindRepository = mockk<SmoothedApparentWindRepository>()
        val magneticHeadingRepository = mockk<MagneticHeadingRepository>()
        val courseOverGroundRepository = mockk<CourseOverGroundRepository>()
        val speedOverGroundRepository = mockk<SpeedOverGroundRepository>()
        val trueWindRepository = mockk<TrueWindRepository>()
        val velocityMadeGoodRepository = mockk<VelocityMadeGoodRepository>()


        val smoothApparentWindPublisher = TestPublisher.create<ApparentWindMeasurement>()
        every { smoothApparentWindRepository.getSmoothApparentWindStream() } returns smoothApparentWindPublisher.flux()

        val magneticHeadingPublisher = TestPublisher.create<MagneticHeadingMeasurement>()
        every { magneticHeadingRepository.getMagneticHeading() } returns magneticHeadingPublisher.flux()

        val courseOverGroundPublisher = TestPublisher.create<CourseOverGroundMeasurement>()
        every { courseOverGroundRepository.getCourseOverGround() } returns courseOverGroundPublisher.flux()

        val speedOverGroundPublisher = TestPublisher.create<SpeedOverGroundMeasurement>()
        every { speedOverGroundRepository.getSpeedOverGround() } returns speedOverGroundPublisher.flux()

        val trueWindPublisher = TestPublisher.create<TrueWindMeasurement>()
        every { trueWindRepository.getTrueWindStream() } returns trueWindPublisher.flux()

        val velocityMadeGoodPublisher = TestPublisher.create<VelocityMadeGoodMeasurement>()
        every { velocityMadeGoodRepository.getVelocityMadeGoodStream() } returns velocityMadeGoodPublisher.flux()

        val testee = CockpitUseCase(
                smoothApparentWindRepository,
                magneticHeadingRepository,
                speedOverGroundRepository,
                courseOverGroundRepository,
                trueWindRepository,
                velocityMadeGoodRepository)

        StepVerifier.create(testee.getCockpit())
                .then { smoothApparentWindPublisher.next(ApparentWindMeasurement(Wind(7.1404906978132, Radiant(1.0)), getTimestampWithDelay(0))) }
                .then { magneticHeadingPublisher.next(MagneticHeadingMeasurement(Radiant(1.0), getTimestampWithDelay(1))) }
                .then { courseOverGroundPublisher.next(CourseOverGroundMeasurement(Radiant(2.3), getTimestampWithDelay(1))) }
                .then { speedOverGroundPublisher.next(SpeedOverGroundMeasurement(4.35, getTimestampWithDelay(1))) }
                .then { trueWindPublisher.next(TrueWindMeasurement(getTimestampWithDelay(1), TrueWind(9.56712376, Radiant(5.12), Radiant(4.3)))) }
                .then { velocityMadeGoodPublisher.next(VelocityMadeGoodMeasurement(getTimestampWithDelay(1), 1.5)) }
                .expectNext(CockpitDto(Wind(7.1404906978132, Radiant(1.0)), TrueWind(9.56712376, Radiant(5.12), Radiant(4.3)), 4.35, Radiant(2.3), Radiant(1.0), 1.5))
                .then { trueWindPublisher.next(TrueWindMeasurement(getTimestampWithDelay(1), TrueWind(9.56712376, Radiant(5.12), Radiant(4.3)))) }
                .expectNext(CockpitDto(Wind(7.1404906978132, Radiant(1.0)), TrueWind(9.56712376, Radiant(5.12), Radiant(4.3)), 4.35, Radiant(2.3), Radiant(1.0), 1.5))

                .then { smoothApparentWindPublisher.next(ApparentWindMeasurement(Wind(1.462874378, Radiant(1.0)), getTimestampWithDelay(2))) }
                .expectNext(CockpitDto(Wind(1.462874378, Radiant(1.0)), TrueWind(9.56712376, Radiant(5.12), Radiant(4.3)), 4.35, Radiant(2.3), Radiant(1.0), 1.5))

                .then { magneticHeadingPublisher.next(MagneticHeadingMeasurement(Radiant(2.5), getTimestampWithDelay(3))) }
                .expectNext(CockpitDto(Wind(1.462874378, Radiant(1.0)), TrueWind(9.56712376, Radiant(5.12), Radiant(4.3)), 4.35, Radiant(2.3), Radiant(2.5), 1.5))

                .then { magneticHeadingPublisher.complete() }
                .then { smoothApparentWindPublisher.complete() }
                .then { courseOverGroundPublisher.complete() }
                .then { speedOverGroundPublisher.complete() }
                .then { trueWindPublisher.complete() }
                .then { velocityMadeGoodPublisher.complete() }
                .verifyComplete()
    }

    private fun getTimestampWithDelay(secondsDelay: Int = 0, minutesDelay: Int = 4): LocalDateTime {
        return LocalDateTime.of(2017, 1, 7, 12, minutesDelay, secondsDelay, 0)
    }
}
