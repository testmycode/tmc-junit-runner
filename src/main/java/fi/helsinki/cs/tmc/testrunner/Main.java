package fi.helsinki.cs.tmc.testrunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    
    private void printUsage() {
        PrintStream out = System.out;
        out.println("Incorrect usage!");
        out.println("1. Give as parameters a list of test methods with points like");
        out.println("  \"fully.qualified.ClassName.methodName{point1,point2,etc}\"");
        out.println();
        out.println("2. Define the following properties (java -Dprop=value)");
        out.println("  tmc.test_class_dir  The place to load tests from.");
        out.println("  tmc.results_file    A file to write results to.");
        out.println();
    }
    

    private String resultsFilename = null;
    private long wholeRunTimeout = 60*1000;

    private String testClassDir = null;

    public static void main(String[] args) {
        try {
            new Main().run(args);
        } catch (Throwable t) {
            System.err.print("Uncaught exception in main thread: ");
            t.printStackTrace(System.err);
        }
        System.exit(0); // Ensure non-daemon threads exit
    }
    
    private Main() {}
    
    private void run(String[] args) throws IOException {
        try {
            readProperties();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println();
            printUsage();
            System.exit(1);
        }
        
        TestCaseList cases = parseTestCases(args);
        runExercises(cases);
        writeResults(cases);
    }

    private void readProperties() {
        testClassDir = requireProperty("tmc.test_class_dir");
        resultsFilename = requireProperty("tmc.results_file");
    }
    
    private String requireProperty(String name) {
        String prop = System.getProperty(name);
        if (prop != null) {
            return prop;
        } else {
            throw new IllegalArgumentException("Missing property: " + name);
        }
    }
    
    private TestCaseList parseTestCases(String[] names) {
        TestCaseList result = new TestCaseList();
        
        Pattern regex = Pattern.compile("^([^{]*)\\.([^.{]*)(?:\\{(.*)\\})?$");
        
        for (String name : names) {
            Matcher matcher = regex.matcher(name);
            if (matcher.matches()) {
                String className = matcher.group(1);
                String methodName = matcher.group(2);
                String pointList = matcher.group(3);
                
                String[] pointNames;
                if (pointList != null && !pointList.isEmpty()) {
                    pointNames = pointList.split(",");
                } else {
                    pointNames = new String[0];
                }
                
                result.add(new TestCase(className, methodName, pointNames));
            } else {
                throw new IllegalArgumentException("Illegal test name: " + name);
            }
        }
        return result;
    }
    
    private void runExercises(TestCaseList cases) {
        TestRunner testRunner = new TestRunner(getTestClassLoader());
        testRunner.runTests(cases, wholeRunTimeout);
    }
    
    private ClassLoader getTestClassLoader() {
        try {
            return new URLClassLoader(new URL[] { new File(testClassDir).toURI().toURL() });
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid test class dir: " + testClassDir);
        }
    }
    
    private void writeResults(TestCaseList cases) throws IOException {
        Writer w = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(resultsFilename)), "UTF-8");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(StackTraceElement.class, new StackTraceSerializer())
                .create();
        w.write(gson.toJson(cases));
        w.close();
    }
}
