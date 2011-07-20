
package fi.helsinki.cs.tmc.testrunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import org.junit.Test;

public class TestRunner {

    private Class testClass;
    private final ArrayList<TestCase> testCases = new ArrayList<TestCase>();

    public TestRunner(Class testClass) {
        this.testClass = testClass;
        listTestCases();
    }

    public List<TestCase> getTestCases() {
        return Collections.unmodifiableList(this.testCases);
    }

    public TreeMap<String, ArrayList<TestCase>> runTests(long timeout) {
        TestRun testRun = new TestRun(this.testClass, this.testCases);

        Thread runnerThread = new Thread(testRun);
        runnerThread.start();
        try {
            runnerThread.join(timeout);
        } catch (InterruptedException ignore) {}

        synchronized(this.testCases) {
            timeoutRunningTestCase();
        	return testCasesToMap();
        }
    }

    private void timeoutRunningTestCase() {
        for (TestCase t : this.testCases) {
            if (t.status == TestCase.TEST_RUNNING) {
                t.status = TestCase.TEST_FAILED;
                t.message = "timeout";
                return;
            }
        }
    }

    private TreeMap<String, ArrayList<TestCase>> testCasesToMap() {
        TreeMap<String, ArrayList<TestCase>> result =
                new TreeMap<String, ArrayList<TestCase>>();

        for (TestCase testCase : this.testCases) {
            for (String pointName : testCase.pointNames) {
                ArrayList<TestCase> pointCases = result.get(pointName);
                if (pointCases == null) {
                	pointCases = new ArrayList<TestCase>();
                    result.put(pointName, pointCases);
                }
                pointCases.add(testCase);
            }
        }

        return result;
    }

    private void listTestCases() {
        for (Method m : this.testClass.getMethods()) {
            Test t = m.getAnnotation(Test.class);
            Exercise annotation = m.getAnnotation(Exercise.class);
            if (t == null || annotation == null) {
                continue;
            }
            TestCase testCase = new TestCase(m.getName(),
                    this.testClass.getName(), annotation.value().split(" +"));
            this.testCases.add(testCase);
        }
    }
}
