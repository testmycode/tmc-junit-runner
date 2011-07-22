package fi.helsinki.cs.tmc.testrunner;

import org.junit.runner.notification.Failure;

public class TestCase {
    public String[] pointNames;
    public String methodName;
    public String className;
    public String message;
    public int status;

    static final int TEST_PASSED = 1;
    static final int TEST_FAILED = 2;
    static final int TEST_RUNNING = 4;
    static final int TEST_NOT_STARTED = 8;

    public TestCase(String methodName, String className, String[] pointNames) {
        this.methodName = methodName;
        this.className = className;
        this.status = TEST_NOT_STARTED;
        this.pointNames = pointNames;
        this.message = null;
    }

    public TestCase(TestCase aTestCase) {
        this.methodName = aTestCase.methodName;
        this.className = aTestCase.className;
        this.message = aTestCase.message;
        this.status = aTestCase.status;
        this.pointNames = aTestCase.pointNames.clone();
    }

    public void testStarted() {
        this.status = TEST_RUNNING;
    }

    public void testFinished() {
        if (this.status != TEST_FAILED) {
            this.status = TEST_PASSED;
        }
    }

    public void testFailed(Failure f) {
        this.message = f.getException().toString();
        this.status = TEST_FAILED;
    }

    public String statusToString() {
        switch (this.status)
        {
            case TEST_FAILED: return "failed";
            case TEST_PASSED: return "passed";
            case TEST_RUNNING: return "running";
            case TEST_NOT_STARTED: return "not started";
            default: return "illegal";
        }
    }

    @Override
    public String toString() {
        String ret = this.methodName + "(" + this.className + ") " +
                statusToString();
        if (this.message != null) {
            ret += ": " + this.message;
        }
        return ret;
    }
}
