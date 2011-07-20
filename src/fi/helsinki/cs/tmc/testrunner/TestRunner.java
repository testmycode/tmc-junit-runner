
package fi.helsinki.cs.tmc.testrunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.TreeMap;
import org.junit.Test;

public class TestRunner {

    private Class testClass;
    private ArrayList<TestCase> testCases = new ArrayList<TestCase>();

    public TestRunner(Class testClass) {
        this.testClass = testClass;
        listTestCases();
    }

    public ArrayList<TestCase> getTestCases() {
        return this.testCases;
    }

    public TreeMap<String, ArrayList<TestCase>> runTests(long timeout) {
        TestRun testRun = new TestRun(this.testClass, this.testCases);

        Thread runnerThread = new Thread(testRun);
        runnerThread.start();
        try {
            runnerThread.join(timeout);
        } catch (InterruptedException ignore) {}

        TreeMap<String, ArrayList<TestCase>> results =
                new TreeMap<String, ArrayList<TestCase>>();

        for (TestCase testCase : this.testCases) {
            for (String pointName : testCase.pointNames) {
                ArrayList<TestCase> pointCases = results.get(pointName);
                if (pointCases == null) {
                	pointCases = new ArrayList<TestCase>();
                    results.put(pointName, pointCases);
                }
                pointCases.add(testCase);
            }
        }

        return results;
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
