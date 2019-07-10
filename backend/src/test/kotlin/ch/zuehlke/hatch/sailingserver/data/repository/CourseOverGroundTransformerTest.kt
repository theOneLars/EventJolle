package ch.zuehlke.hatch.sailingserver.data.repository

import ch.zuehlke.hatch.sailingserver.data.eventstore.EventTransformer
import ch.zuehlke.hatch.sailingserver.domain.CourseOverGroundMeasurement
import ch.zuehlke.hatch.sailingserver.domain.Radiant
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class CourseOverGroundTransformerTest {

    private var transformer: EventTransformer<CourseOverGroundMeasurement> = CourseOverGroundTransformer()

    @Test
    fun testTransformWithEmptyJson() {
        val jsonObject = givenEmptyJson()

        val actual = this.transformer.transform(jsonObject)

        assertThat(actual)
                .isEmpty()
    }

    @Test
    fun testTransformWithValidCourseOverGround() {
        val jsonObject = givenJsonWithValidCourseOverGround()

        val actual = this.transformer.transform(jsonObject)

        assertThat(actual)
                .containsExactly(
                        CourseOverGroundMeasurement(Radiant(5.625021646252525), LocalDateTime.of(2017, 8, 12, 4, 19, 30)))
    }

    @Test
    fun testTransformWithZeroCourseOverGround() {
        val jsonObject = givenJsonWithZeroCourseOverGround()

        val actual = this.transformer.transform(jsonObject)

        assertThat(actual)
                .containsExactly(
                        CourseOverGroundMeasurement(Radiant(0.0), LocalDateTime.of(2017, 8, 12, 4, 19, 30)))
    }

    private fun givenJsonObject(json: String): JsonObject {
        val parser = JsonParser()
        return parser.parse(json).asJsonObject
    }

    private fun givenEmptyJson(): JsonObject {
        return givenJsonObject("{\"updates\":[{\"values\":[{}]}]}")
    }

    private fun givenJsonWithValidCourseOverGround(): JsonObject {
        return givenJsonObject("{\"updates\":[{\"timestamp\":\"2017-08-12T04:19:30.000Z\",\"values\":[{\"path\":\"navigation.courseOverGroundTrue\",\"value\":5.625021646252525}]}]}")
    }

    private fun givenJsonWithZeroCourseOverGround(): JsonObject {
        return givenJsonObject("{\"updates\":[{\"timestamp\":\"2017-08-12T04:19:30.000Z\",\"values\":[{\"path\":\"navigation.courseOverGroundTrue\",\"value\":0}]}]}")
    }
}
