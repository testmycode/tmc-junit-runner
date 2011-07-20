package fi.helsinki.cs.tmc.testrunner;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.model.InitializationError;

public class Main {

    private static final int ACTION_USAGE = 0;
    private static final int ACTION_LIST = 1;
    private static final int ACTION_RUN = 2;

    private static PrintStream outStream = System.out;
    private static PrintStream resultsStream = System.out;
    private static String outFilename = null;
    private static String resultsFilename = null;
    private static long timeout = 60*1000;

    private static int action = ACTION_USAGE;
    private static String classPath = null;
    private static String className = null;

    private static URLClassLoader classLoader = null;
    private static Class testClass = null;

    public static void main(String[] args) throws
            MalformedURLException, ClassNotFoundException,
            InitializationError, NoTestsRemainException,
            FileNotFoundException {

        parseArguments(args);
        redirectOutput();

        switch (Main.action) {
            case ACTION_LIST:
            	listExercises();
                break;

            case ACTION_RUN:
            	runExercises();
                break;

            default: case ACTION_USAGE:
                usage();
                break;
        }
    }

    private static void redirectOutput() throws FileNotFoundException {
        if (Main.outFilename != null) {
            Main.outStream =
                    new PrintStream(new FileOutputStream(Main.outFilename));
            System.setErr(Main.outStream);
            System.setOut(Main.outStream);
        }
        if (Main.resultsFilename != null) {
            Main.resultsStream =
                    new PrintStream(new FileOutputStream(Main.resultsFilename));
        }
    }

    private static void parseArguments(String args[]) {
        if (args.length < 3) {
            Main.action = ACTION_USAGE;
            return;
        }

        if (args[0].equals("list")) {
            Main.action = ACTION_LIST;
        } else if (args[0].equals("run")) {
            Main.action = ACTION_RUN;
        } else {
            Main.action = ACTION_USAGE;
        }

        Main.classPath = args[1];
        Main.className = args[2];

        if (args.length >= 4) {
            try {
                Main.timeout = Integer.parseInt(args[3]) * 1000;
            } catch (NumberFormatException e) {}
        }

        if (args.length >= 5) {
            Main.resultsFilename = args[4];
        }

        if (args.length >= 6) {
            Main.outFilename = args[5];
        }
    }

    private static void createClassLoader() throws MalformedURLException {
        URL[] urls = { new File(classPath).toURI().toURL() };
        Main.classLoader = new URLClassLoader(urls);
    }

    private static void loadTestClass() throws MalformedURLException,
            ClassNotFoundException {
        createClassLoader();
        Main.testClass = Main.classLoader.loadClass(Main.className);
    }

    private static void runExercises() throws MalformedURLException,
            ClassNotFoundException, InitializationError,
            NoTestsRemainException {
        TMCSecurityManager.setupSM(Main.classPath, "testrunner.policy");
        loadTestClass();
        TestRunner runner = new TestRunner(Main.testClass);
        Gson gson = new Gson();
        TreeMap<String, ArrayList<TestResult>> results =
                runner.runTests(Main.timeout);
        resultsStream.println(gson.toJson(results));
    }

    private static void listExercises() throws MalformedURLException,
            ClassNotFoundException {
        loadTestClass();
        TestRunner runner = new TestRunner(Main.testClass);
        TreeSet<String> exercises = runner.getExercises();
        Gson gson = new Gson();
        resultsStream.println(gson.toJson(exercises));
    }

    private static void usage() {
        System.out.println("Usage: ./testrunner <list|run> classpath" +
                " classname timeout resultsfile outfile");
    }

    private Main() {}
}
