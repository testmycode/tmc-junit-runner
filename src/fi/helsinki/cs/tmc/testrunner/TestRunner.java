
package fi.helsinki.cs.tmc.testrunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
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

    public ArrayList<TestCase> getTestCases() {
        return this.testCases.clone();
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

    private void createTestCases(Class testClass) {
        for (Method m : testClass.getMethods()) {
            Test t = m.getAnnotation(Test.class);
            Exercise annotation = m.getAnnotation(Exercise.class);
            if (t == null || annotation == null) {
                continue;
            }
            TestCase testCase = new TestCase(m.getName(), testClass.getName(),
                    annotation.value().split(" +"));
            this.testCases.add(testCase);
        }
    }

    private void timeoutRunningTestCase() {
        if (this.currentCase < 0 || this.currentCase >= this.testCases.size()) {
            return;
        }
        TestCase t = getCurrentCase();
        if (t.status == TestCase.TEST_RUNNING) {
            t.status = TestCase.TEST_FAILED;
            t.message = "timeout";
            return;
        }
    }

}
