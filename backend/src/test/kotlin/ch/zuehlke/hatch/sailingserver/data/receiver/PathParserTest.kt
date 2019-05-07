package ch.zuehlke.hatch.sailingserver.data.receiver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PathParserTest {

    private lateinit var parser: PathParser;

    @BeforeEach
    fun setUp() {
        this.parser = PathParser()
    }

    @Test
    fun testParseWithSimplePath() {
        // Execute the test case
        val actual = this.parser.parse("_id");

        // Verify the test results
        assertThat(actual)
                .isEqualTo(Path(listOf(PathElement("_id", null))))
    }

    @Test
    fun testParseWithSimplePathAndIndex() {
        // Execute the test case
        val actual = this.parser.parse("updates[0]");

        // Verify the test results
        assertThat(actual)
                .isEqualTo(Path(listOf(PathElement("updates", 0))))
    }

    @Test
    fun testParseWithStructuredPath() {
        // Execute the test case
        val actual = this.parser.parse("updates[0].values[0].path");

        // Verify the test results
        assertThat(actual)
                .isEqualTo(Path(listOf(PathElement("updates", 0),
                        PathElement("values", 0),
                        PathElement("path", null))))
    }
}