package fi.helsinki.cs.tmc.testrunner;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestRunnerTest {
    @Test
    public void shouldReturnTestResults() throws Exception {
        TestRunner testRunner = new TestRunner(TestRunnerTestSubject.class);
        TestCases allCases = testRunner.runTests(5000);

        TestCases seekResults =
                allCases.findByMethodName("successfulTestCaseForOneTwoThree");
        assertEquals(1, seekResults.size());
        TestCase testCase = seekResults.get(0);
        assertEquals(TestCase.TEST_PASSED, testCase.status);

        seekResults = allCases.findByMethodName("successfulTestCaseForTwo");
        assertEquals(1, seekResults.size());
        testCase = seekResults.get(0);
        assertEquals(TestCase.TEST_FAILED, testCase.status);
    }

    @Test
    public void shouldTimeout() throws Exception {
        TestRunner testCases = new TestRunner(TimeoutTestSubject.class);
		testCases.runTests(1000);
        ArrayList<TestCase> results = testCases.getTestCases();

        TestCase infiniteCase = results.get(0);

        assertEquals("infinite", infiniteCase.methodName);
        assertEquals(TestCase.TEST_FAILED, infiniteCase.status);
        assertTrue(infiniteCase.message.contains("timeout"));
    }

}
