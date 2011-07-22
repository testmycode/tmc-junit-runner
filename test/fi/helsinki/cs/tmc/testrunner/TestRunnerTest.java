package fi.helsinki.cs.tmc.testrunner;

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

        seekResults = allCases.findByPointName("one");
        assertEquals(1, seekResults.size());

        seekResults = allCases.findByPointName("two");
        assertEquals(2, seekResults.size());

        seekResults = allCases.findByPointName("three");
        assertEquals(1, seekResults.size());
    }

    @Test
    public void shouldTimeoutInfiniteLoop() throws Exception {
        TestRunner testRunner = new TestRunner(TimeoutTestSubject.class);
        TestCases allCases = testRunner.runTests(1000);

        assertEquals(3, allCases.size());
        TestCase infiniteCase = allCases.findByMethodName("infinite").get(0);

        assertEquals("infinite", infiniteCase.methodName);
        assertEquals(TestCase.TEST_FAILED, infiniteCase.status);
        assertTrue(infiniteCase.message.contains("timeout"));

        TestCases passingCases = allCases.findByPointName("passing");
        assertEquals(2, passingCases.size());
        for (TestCase t : passingCases) {
            assertTrue(t.status == TestCase.TEST_NOT_STARTED
                    || t.status == TestCase.TEST_PASSED);
        }

    }
}

