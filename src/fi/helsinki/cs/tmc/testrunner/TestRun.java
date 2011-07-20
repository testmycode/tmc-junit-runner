
package fi.helsinki.cs.tmc.testrunner;

import java.util.ArrayList;
import java.util.TreeMap;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class TestRun implements Runnable {

    private ArrayList<TestCase> testCases;
    private Class testClass;

    public TestRun(Class testClass, ArrayList<TestCase> testCases) {
        this.testClass = testClass;
        this.testCases = testCases;
    }

    public void run() {
        try {
            BlockJUnit4ClassRunner runner =
                    new BlockJUnit4ClassRunner(this.testClass);
            for (TestCase testCase : this.testCases) {
                RunNotifier notifier = new RunNotifier();
                TestListener listener = new TestListener(this.testCases);
                notifier.addFirstListener(listener);
                MethodFilter filter = new MethodFilter(testCase.methodName);
                runner.filter(filter);
                runner.run(notifier);
            }
        } catch (NoTestsRemainException ex) {
        } catch (InitializationError ex) {
        }
    }
}
