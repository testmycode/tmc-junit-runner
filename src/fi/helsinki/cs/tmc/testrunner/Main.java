package fi.helsinki.cs.tmc.testrunner;

import fi.helsinki.cs.tmc.testrunner.runner.TestRunner;
import fi.helsinki.cs.tmc.testrunner.runner.TestResult;
import com.google.gson.Gson;
import fi.helsinki.cs.tmc.testrunner.runner.TMCSecurityManager;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.TreeMap;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.model.InitializationError;

public class Main {

    private static PrintStream outStream = new PrintStream(System.out);
    private static PrintStream resultsStream = outStream;

    public static void main(String[] args) throws
            MalformedURLException, ClassNotFoundException,
            InitializationError, NoTestsRemainException,
            FileNotFoundException {

        if (args.length < 3) {
            usage();
            return;
        } else if (args.length >= 5) {
            outStream =
                    new PrintStream(new FileOutputStream(args[3]));
            resultsStream =
                    new PrintStream(new FileOutputStream(args[4]));
            System.setErr(outStream);
            System.setOut(outStream);
        }

        long timeout = 60;
        if (args.length >= 6)
{
            try {
                timeout = Integer.parseInt(args[5]);
            } catch (NumberFormatException e) {}
        }
        timeout *= 1000;

        String command = args[0];
        String classpath = args[1];
        String classname = args[2];
        TMCSecurityManager.setupSM(classpath, "testrunner.policy");

        if (command.equals("list")) {
            listExercises(classpath, classname);
        } else if (command.equals("run")) {
            runExercises(classpath, classname, timeout);
        } else {
            usage();
            return;
        }

        resultsStream.close();
        outStream.close();
    }

    private static void runExercises(String classpath, String classname,
            long timeout)
            throws MalformedURLException, ClassNotFoundException,
            InitializationError, NoTestsRemainException
{
        TestRunner runner = new TestRunner(classpath, classname);
        Gson gson = new Gson();
        TreeMap<String, ArrayList<TestResult>> results =
                runner.runTests(timeout);
        resultsStream.println(gson.toJson(results));
    }

    private static void usage()
{
        outStream.println("Usage: ./testrunner <list|run> classpath" +
                " classname outfile resultsfile <timeout>");
    }

    private static void listExercises(String classpath, String classname)
            throws MalformedURLException, ClassNotFoundException
{
        //TestRunner runner = new TestRunner(classpath, classname);
        //TreeSet<String> exercises = runner.listExercises();
        //Gson gson = new Gson();
        //resultsStream.println(gson.toJson(exercises));
    }

    private Main() {
    }

}
