package ch.zuehlke.hatch.sailingserver;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class SimulatorRunnerTest {

    @Test
    public void testCommandLineParser() {
        // given
        String[] args = {"-f", "someFile"};
        // when
        Map<String, String> result = SimulatorRunner.parseParameter(args);
        // then
        assertEquals(1, result.size());
        assertTrue(result.containsKey("f"));
        assertEquals("someFile", result.get("f"));
    }
}
