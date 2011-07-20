
package fi.helsinki.cs.tmc.testrunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Test;

public class TestRunner {

    private Class testClass;
    private TreeSet<String> exercises = new TreeSet<String>();

    public TestRunner(Class testClass) {
        this.testClass = testClass;
        listExercises();
    }

    public TreeSet<String> getExercises() {
        return this.exercises;
    }

    public TreeMap<String, ArrayList<TestResult>> runTests(long timeout) {
        TreeMap<String, ArrayList<TestResult>> results =
                new TreeMap<String, ArrayList<TestResult>>();
        TestRun testRun = new TestRun(this.testClass, exercises, results);

        Thread runnerThread = new Thread(testRun);
        runnerThread.start();
        try {
            runnerThread.join(timeout);
        } catch (InterruptedException ignore) {}

        return results;
    }

    private void listExercises() {
        for (Method m : this.testClass.getMethods()) {
            Test t = m.getAnnotation(Test.class);
            if (t == null) {
                continue;
            }

            Exercise annotation = m.getAnnotation(Exercise.class);
            if (annotation != null) {
                exercises.addAll(Arrays.asList(annotation.value().split(" +")));
            }
        }
    }
}
