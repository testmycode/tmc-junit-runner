package fi.helsinki.cs.tmc.testrunner;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestRunnerTest {
    @Test
    public void shouldReturnTestResults() throws Exception {
        TestCaseList allCases = new TestCaseList();
        allCases.add(new TestCase(
                TestRunnerTestSubject.class.getName(), "successfulTestCaseForOneTwoThree",
                new String[] { "one", "two", "three" }
                ));
        allCases.add(new TestCase(
                TestRunnerTestSubject.class.getName(), "failingTestCaseForTwo",
                new String[] { "two" }
                ));
        
        TestRunner testRunner = new TestRunner(this.getClass().getClassLoader());
        testRunner.runTests(allCases, 5000);

        TestCaseList seekResults =
                allCases.findByMethodName("successfulTestCaseForOneTwoThree");
        assertEquals(1, seekResults.size());
        TestCase testCase = seekResults.get(0);
        assertEquals(TestCase.TEST_PASSED, testCase.status);

        seekResults = allCases.findByMethodName("failingTestCaseForTwo");
        assertEquals(1, seekResults.size());
        testCase = seekResults.get(0);
        assertEquals(TestCase.TEST_FAILED, testCase.status);

        seekResults = allCases.findByPointName("one");
        assertEquals(1, seekResults.size());

        seekResults = allCases.findByPointName("two");
        assertEquals(2, seekResults.size());

        seekResults = allCases.findByPointName("three");
        assertEquals(1, seekResults.size());
        
        seekResults = allCases.findByPointName("ninethousand");
        assertTrue(seekResults.isEmpty());
    }

    @Test
    public void shouldTimeoutInfiniteLoop() throws Exception {
        TestCaseList allCases = new TestCaseList();
        allCases.add(new TestCase(
                TimeoutTestSubject.class.getName(), "infinite",
                new String[] { "infinite" }
                ));
        allCases.add(new TestCase(
                TimeoutTestSubject.class.getName(), "empty",
                new String[] { "passing" }
                ));
        allCases.add(new TestCase(
                TimeoutTestSubject.class.getName(), "empty2",
                new String[] { "passing" }
                ));
        
        TestRunner testRunner = new TestRunner(this.getClass().getClassLoader());
        testRunner.runTests(allCases, 1000);

        assertEquals(3, allCases.size());
        TestCase infiniteCase = allCases.findByMethodName("infinite").get(0);

        assertEquals("infinite", infiniteCase.methodName);
        assertEquals(TestCase.TEST_FAILED, infiniteCase.status);
        assertTrue(infiniteCase.message.contains("timeout"));

        TestCaseList passingCases = allCases.findByPointName("passing");
        assertEquals(2, passingCases.size());
        for (TestCase t : passingCases) {
            assertTrue(t.status == TestCase.TEST_NOT_STARTED
                    || t.status == TestCase.TEST_PASSED);
        }

    }
}

