package fi.helsinki.cs.tmc.testrunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class TestRunner implements Runnable {

    private TestCases testCases = new TestCases();
    private final Object lock = new Object();
    private boolean stop;
    private int currentCase;
    private Class testClass;

    public TestRunner(Class testClass) {
        this.testClass = testClass;
        this.currentCase = -1;
        this.stop = false;
        createTestCases(testClass);
    }

    public TestCase getCurrentCase() {
        return this.testCases.get(this.currentCase);
    }

    public TestCases runTests(long timeout) {
        Thread runnerThread = new Thread(this);

        runnerThread.start();
        try {
            runnerThread.join(timeout);
        } catch (InterruptedException ignore) {}

        synchronized(this.lock) {
            this.stop = true;
            timeoutRunningTestCase();
            return this.testCases.clone();
        }
    }

    public void run() {
        try {
            BlockJUnit4ClassRunner runner =
                    new BlockJUnit4ClassRunner(this.testClass);
            for (this.currentCase = 0;
                this.currentCase < this.testCases.size();
                this.currentCase++) {
                TestCase testCase = getCurrentCase();
                RunNotifier notifier = new RunNotifier();
                notifier.addFirstListener(new TestListener(testCase, lock));
                runner.filter(new MethodFilter(testCase.methodName));
                runner.run(notifier);

                if (this.stop) {
                    break;
                }
            }
        } catch (NoTestsRemainException ex) {
        } catch (InitializationError ex) {
        }
    }

    private void createTestCases(Class<?> testClass) {
        for (Method m : testClass.getMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                List<String> points = pointsOfTestCase(m);
                if (!points.isEmpty()) {
                    TestCase testCase = new TestCase(m.getName(), testClass.getName(), points.toArray(new String[0]));
                    this.testCases.add(testCase);
                }
            }
        }
    }
    
    private List<String> pointsOfTestCase(Method m) {
        ArrayList<String> pointNames = new ArrayList<String>();
        Points classAnnotation = m.getDeclaringClass().getAnnotation(Points.class);
        if (classAnnotation != null) {
            pointNames.addAll(Arrays.asList(classAnnotation.value().split(" +")));
        }
        Points methodAnnotation = m.getAnnotation(Points.class);
        if (methodAnnotation != null) {
            pointNames.addAll(Arrays.asList(methodAnnotation.value().split(" +")));
        }
        return pointNames;
    }

    private void timeoutRunningTestCase() {
        if (this.currentCase < 0 ||
            this.currentCase >= this.testCases.size()) {
            return;
        }

        TestCase t = getCurrentCase();
        if (t.status == TestCase.TEST_RUNNING) {
            t.status = TestCase.TEST_FAILED;
            t.message = "timeout";
        }
    }
}

