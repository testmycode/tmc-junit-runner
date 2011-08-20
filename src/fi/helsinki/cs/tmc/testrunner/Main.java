package fi.helsinki.cs.tmc.testrunner;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.model.InitializationError;

public class Main {

    private static final int ACTION_USAGE = 0;
    private static final int ACTION_RUNTESTS = 1;

    private static PrintStream outStream = System.out;
    private static PrintStream resultsStream = System.out;
    private static String outFilename = null;
    private static String resultsFilename = null;
    private static String policyFilename = null;
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
            case ACTION_RUNTESTS:
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
        if (args.length < 2) {
            Main.action = ACTION_USAGE;
            return;
        }

        Main.action = ACTION_RUNTESTS;
        Main.classPath = args[0];
        Main.className = args[1];

        if (args.length >= 3) {
            try {
                Main.timeout = Integer.parseInt(args[2]) * 1000;
            } catch (NumberFormatException e) {}
        }

        if (args.length >= 4) {
            Main.resultsFilename = args[3];
        }

        if (args.length >= 5) {
            Main.outFilename = args[4];
        }

        if (args.length >= 6) {
            Main.policyFilename = args[5];
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
        TMCSecurityManager.setupSecurityManager(Main.classPath,
                Main.policyFilename);
        loadTestClass();
        TestRunner testCases = new TestRunner(Main.testClass);
        TestCases results = testCases.runTests(Main.timeout);
        resultsStream.println(new Gson().toJson(results));
        System.exit(0);
    }

    private static void usage() {
        System.out.println("Usage: ./testrunner classpath" +
                " classname timeout resultsfile outfile policyfile");
    }

    private Main() {}
}

