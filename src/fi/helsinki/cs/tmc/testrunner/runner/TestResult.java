package fi.helsinki.cs.tmc.testrunner.runner;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

public class TestResult
{
	public String methodName;
	public String className;
	public String message;
	public int status;

	static final int TEST_FAILED = 0;
	static final int TEST_PASSED = 1;
	static final int TEST_RUNNING = 2;

	public TestResult(Description description)
	{
		this.methodName = description.getMethodName();
		this.className = description.getClassName();
		this.message = "";
		this.status = TEST_RUNNING;
	}

	public void testFinished()
	{
		if (this.status != TEST_FAILED) {
			this.status = TEST_PASSED;
		}
	}

	public void testFailed(Failure f)
	{
		this.message = f.getException().toString();
		this.status = TEST_FAILED;
		//System.out.println(f.getTrace().toString());
	}

	public String statusToString()
	{
		if (this.status == TEST_FAILED) {
			return "failed";
		} else if (this.status == TEST_PASSED) {
			return "passed";
		} else {
			return "running";
		}
	}

	@Override
	public String toString()
	{
		String ret = this.methodName + "(" + this.className + ") " +
			statusToString();
		if (!this.message.isEmpty()) {
			ret += ": " + this.message;
		}
		return ret;

	}
}
