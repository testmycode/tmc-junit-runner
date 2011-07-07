/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.tmc.testrunner;

import fi.helsinki.cs.tmc.testrunner.runner.TestRunner;
import fi.helsinki.cs.tmc.testrunner.runner.TestResult;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.model.InitializationError;

/**
 *
 * @author mrannanj
 */
public class Main {

	private static PrintStream outStream = new PrintStream(System.out);
	private static PrintStream resultsStream = outStream;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws
                MalformedURLException, ClassNotFoundException,
                InitializationError, NoTestsRemainException,
                FileNotFoundException
	{
		if (args.length < 3)
		{
			usage();
			return;
		}
		else if (args.length >= 5)
		{
			outStream =
				new PrintStream(new FileOutputStream(args[3]));
			resultsStream =
				new PrintStream(new FileOutputStream(args[4]));
			System.setErr(outStream);
			System.setOut(outStream);
		}

		String command = args[0];
		String classpath = args[1];
		String classname = args[2];

		if (command.equals("list")) {
			listExercises(classpath, classname);
		} else if (command.equals("run")) {
			runExercises(classpath, classname);
		} else {
			usage();
			return;
		}

		resultsStream.close();
		outStream.close();
	}

	private static void usage()
	{
		outStream.println("Usage: ./testrunner <list|run> classpath classname");
	}

	private static void listExercises(String classpath, String classname)
		throws MalformedURLException, ClassNotFoundException
	{
		//TestRunner runner = new TestRunner(classpath, classname);
		//TreeSet<String> exercises = runner.listExercises();
		//Gson gson = new Gson();
		//resultsStream.println(gson.toJson(exercises));
	}

	private static void runExercises(String classpath, String classname)
		throws MalformedURLException, ClassNotFoundException,
		InitializationError, NoTestsRemainException
	{
		TestRunner runner = new TestRunner(classpath, classname);
		TreeMap<String, ArrayList<TestResult>> results =
			runner.runTests();
		Gson gson = new Gson();
		resultsStream.println(gson.toJson(results));
	}
}
