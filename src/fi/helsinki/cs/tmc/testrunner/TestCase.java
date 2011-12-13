package fi.helsinki.cs.tmc.testrunner;

import org.junit.runner.notification.Failure;
import static fi.helsinki.cs.tmc.testrunner.TestCaseStatus.*;

public class TestCase {
    public String className;
    public String methodName;
    public String[] pointNames;
    public String message;
    public StackTraceElement[] stackTrace;
    public TestCaseStatus status;

    public TestCase(String className, String methodName, String[] pointNames) {
        this.methodName = methodName;
        this.className = className;
        this.status = NOT_STARTED;
        this.pointNames = pointNames;
        this.message = null;
        this.stackTrace = null;
    }

    public TestCase(TestCase aTestCase) {
        this.methodName = aTestCase.methodName;
        this.className = aTestCase.className;
        this.message = aTestCase.message;
        this.status = aTestCase.status;
        this.pointNames = aTestCase.pointNames.clone();
        this.stackTrace = aTestCase.stackTrace.clone();
    }

    public void testStarted() {
        this.status = RUNNING;
    }

    public void testFinished() {
        if (this.status != FAILED) {
            this.status = PASSED;
        }
    }

    public void testFailed(Failure f) {
        this.message = failureMessage(f);
        this.status = FAILED;
        
        Throwable ex = f.getException();
        if (ex != null) {
            stackTrace = ex.getStackTrace();
        }
    }
    
    private String failureMessage(Failure f) {
        String msg = "";
        if (f.getMessage() != null && !f.getMessage().isEmpty()) {
            if (!(f.getException() instanceof AssertionError)) {
                msg += f.getException().getClass().getSimpleName() + ": ";
            }
            msg += f.getMessage();
        }
        if (!msg.isEmpty()) {
            return msg;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        String ret = this.methodName + " (" + this.className + ") " + status;
        if (this.message != null) {
            ret += ": " + this.message;
        }
        if (this.stackTrace != null) {
            ret += "\n";
            for (StackTraceElement ste : this.stackTrace) {
                ret += ste.toString() + "\n";
            }
        }
        return ret;
    }
}
