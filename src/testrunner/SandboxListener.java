package testrunner;

import java.util.ArrayList;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mrannanj
 */
public class SandboxListener extends RunListener {

	ArrayList<TestResult> results;

	public SandboxListener(ArrayList<TestResult> results)
	{
		this.results = results;
	}

	private TestResult getTestResult(Description desc) throws Exception
	{
		for (TestResult r : this.results)
		{
			if (!r.methodName.equals(desc.getMethodName()))
				continue;
			if (!r.className.equals(desc.getClassName()))
				continue;
			return r;
		}
		throw new Exception("TestResult not found.");
	}


	/**
	 * Called before any tests have been run.
	 * @param description describes the tests to be run
	 */
	@Override
		public void testRunStarted(Description description)
		throws Exception
	{
	}

	/**
	 * Called when all tests have finished
	 * @param result the summary of the test run, including all the tests
	 * that failed
	 */
	@Override
		public void testRunFinished(Result result) throws Exception
	{
	}

	/**
	 * Called when an atomic test is about to be started.
	 * @param description the description of the test that is about to be run
	 * (generally a class and method name)
	 */
	@Override
		public void testStarted(Description description) throws Exception
	{
		TestResult result = new TestResult(description);
		results.add(result);
		System.out.println(result);
	}

	/**
	 * Called when an atomic test has finished, whether the test succeeds
	 * or fails.
	 * @param description the description of the test that just ran
	 */
	@Override
		public void testFinished(Description description) throws Exception
	{
		TestResult result = getTestResult(description);
		result.testFinished();
		System.out.println(result);
	}

	/**
	 * Called when an atomic test fails.
	 * @param failure describes the test that failed and the exception
	 * that was thrown
	 */
	@Override
	public void testFailure(Failure failure) throws Exception
	{
		TestResult result = getTestResult(failure.getDescription());
		result.testFailed(failure);
		System.out.println(result);
	}

	/**
	 * Called when a test will not be run, generally because a test method
	 * is annotated with {@link org.junit.Ignore}.
	 * @param description describes the test that will not be run
	 */
	@Override
	public void testIgnored(Description description) throws Exception
	{
	}
}

