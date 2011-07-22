package fi.helsinki.cs.tmc.testrunner;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class TestListener extends RunListener {

    private final TestCase testCase;
    private final Object lock;

    public TestListener(TestCase testCase, Object lock) {
        this.testCase = testCase;
        this.lock = lock;
    }

    /**
     * Called before any tests have been run.
     * @param description describes the tests to be run
     * @throws Exception
     */
    @Override
    public void testRunStarted(Description description) throws Exception {}

    /**
     * Called when all tests have finished
     * @param result the summary of the test run, including all the tests
     * that failed
     * @throws Exception
     */
    @Override
    public void testRunFinished(Result result) throws Exception {}

    /**
     * Called when an atomic test is about to be started.
     * @param desc the description of the test that is about to be run
     * (generally a class and method name)
     * @throws Exception
     */
    @Override
    public void testStarted(Description desc) throws Exception {
        synchronized (this.lock) {
            this.testCase.testStarted();
        }
    }

    /**
     * Called when an atomic test has finished, whether the test succeeds
     * or fails.
     * @param description the description of the test that just ran
     * @throws Exception
     */
    @Override
    public void testFinished(Description description) throws Exception {
        synchronized (this.lock) {
        	this.testCase.testFinished();
        }
    }

    /**
     * Called when an atomic test fails.
     * @param failure describes the test that failed and the exception
     * that was thrown
     * @throws Exception
     */
    @Override
    public void testFailure(Failure failure) throws Exception {
        synchronized (this.lock) {
            this.testCase.testFailed(failure);
        }
    }

    /**
     * Called when a test will not be run, generally because a test method
     * is annotated with {@link org.junit.Ignore}.
     * @param description describes the test that will not be run
     * @throws Exception
     */
    @Override
    public void testIgnored(Description description) throws Exception {}
}
