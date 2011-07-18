
package fi.helsinki.cs.tmc.testrunner.runner;

import fi.helsinki.cs.tmc.testrunner.util.TMCClassLoader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.TreeMap;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.model.InitializationError;

public class TestRunner {

	private Class testClass;
	private ClassLoader classLoader;

	public TestRunner(String testClassPath, String testClassName)
		throws MalformedURLException, ClassNotFoundException
	{
		this.classLoader = TMCClassLoader.fromPath(testClassPath);
		this.testClass = this.classLoader.loadClass(testClassName);
	}


	public TreeMap<String, ArrayList<TestResult>> runTests(long timeout)
		throws InitializationError, NoTestsRemainException
	{
		TreeMap<String, ArrayList<TestResult>> result =
			new TreeMap<String, ArrayList<TestResult>>();

		RunnerThread runnerThread =
			new RunnerThread(this.testClass, result);

		runnerThread.start();
		try {
			runnerThread.join(timeout);
		} catch (InterruptedException ignore) {
		}

		return result;
	}

}
