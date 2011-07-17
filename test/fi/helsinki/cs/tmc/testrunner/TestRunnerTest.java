package fi.helsinki.cs.tmc.testrunner;

import java.util.ArrayList;
import java.util.TreeMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestRunnerTest {
    @Test
    public void shouldReturnTestResultsSortedByAnnotation() throws Exception {
        TestRunner runner = new TestRunner(TestRunnerTestSubject.class);
        TreeMap<String, ArrayList<TestResult>> results = runner.runTests();
        
        assertTrue(results.containsKey("one"));
        assertTrue(results.containsKey("two"));
        assertTrue(results.containsKey("three"));
        assertEquals("successfulTestCaseForOneTwoThree", results.get("one").get(0).methodName);
        assertEquals(TestResult.TEST_PASSED, results.get("one").get(0).status);
        
        assertEquals("successfulTestCaseForOneTwoThree", results.get("two").get(0).methodName);
        assertEquals(TestResult.TEST_PASSED, results.get("two").get(0).status);
        assertEquals("successfulTestCaseForTwo", results.get("two").get(1).methodName);
        assertEquals(TestResult.TEST_FAILED, results.get("two").get(1).status);
    }
}
