package ch.zuehlke.hatch.sailingserver.processing

import ch.zuehlke.hatch.sailingserver.domain.PositionMeasurement
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.Duration
import java.time.LocalDateTime

internal class PositionFilterTest {

    private lateinit var filter: PositionFilter

    @BeforeEach
    fun setUp() {
        this.filter = PositionFilter(Duration.ofSeconds(5))
    }

    @Test
    fun testAcceptWithFirstPosition() {
        // Setup the test case
        val position = PositionMeasurement(LocalDateTime.now(), 1.0, 1.5)

        // Execute the test case
        val actual = this.filter.accept(position)

        // Verify the test results
        assertThat(actual)
                .isTrue()
    }

    @Test
    fun testAcceptWithPositionWithinFilterInterval() {
        // Setup the test case
        this.filter.lastTimestamp = LocalDateTime.now().minusSeconds(1)
        val position = PositionMeasurement(LocalDateTime.now(), 1.0, 1.5)

        // Execute the test case
        val actual = this.filter.accept(position)

        // Verify the test results
        assertThat(actual)
                .isFalse()
    }

    @Test
    fun testAcceptWithPositionOutsideFilterInterval() {
        // Setup the test case
        this.filter.lastTimestamp = LocalDateTime.now().minusSeconds(7)
        val position = PositionMeasurement(LocalDateTime.now(), 1.0, 1.5)

        // Execute the test case
        val actual = this.filter.accept(position)

        // Verify the test results
        assertThat(actual)
                .isTrue()
    }
}