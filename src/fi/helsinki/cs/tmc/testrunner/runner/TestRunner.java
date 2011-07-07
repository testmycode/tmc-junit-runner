/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.helsinki.cs.tmc.testrunner.runner;

import fi.helsinki.cs.tmc.testrunner.annotation.Exercise;
import fi.helsinki.cs.tmc.testrunner.util.ClassLoader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Test;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 *
 * @author mrannanj
 */
public class TestRunner {

	private Class testClass;

	public TestRunner(String testClassPath, String testClassName)
		throws MalformedURLException, ClassNotFoundException
	{
		this.testClass =
			ClassLoader.loadClass(testClassPath, testClassName);
	}


	public TreeMap<String, ArrayList<TestResult>> runTests()
		throws InitializationError, NoTestsRemainException
	{
		TreeMap<String, ArrayList<TestResult>> result =
			new TreeMap<String, ArrayList<TestResult>>();

		RunnerThread runnerThread =
			new RunnerThread(this.testClass, result);

		runnerThread.start();
		try {
			runnerThread.join();
		} catch (InterruptedException ignore) {
		}

		return result;
	}

}
