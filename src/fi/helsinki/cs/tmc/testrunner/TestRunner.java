package fi.helsinki.cs.tmc.testrunner;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Test;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class TestRunner {

    private Class<?> testClass;

    public TestRunner(String testClassPath, String testClassName)
            throws MalformedURLException, ClassNotFoundException {
        loadTestClass(testClassPath, testClassName);
    }
    
    public TestRunner(Class<?> testClass)
            throws MalformedURLException, ClassNotFoundException {
        this.testClass = testClass;
    }

    private void loadTestClass(String testClassPath, String testClassName)
            throws MalformedURLException, ClassNotFoundException {
        File myFile = new File(testClassPath);
        URL[] urls = {myFile.toURI().toURL()};
        ClassLoader cl = new URLClassLoader(urls);
        this.testClass = cl.loadClass(testClassName);
    }

    public TreeSet<String> listExercises() {
        TreeSet<String> exercises = new TreeSet<String>();

        for (Method m : this.testClass.getMethods()) {
            Test t = m.getAnnotation(Test.class);
            if (t == null) {
                continue;
            }

            Exercise annotation = m.getAnnotation(Exercise.class);
            if (annotation != null) {
                exercises.addAll(Arrays.asList(annotation.value().split(" +")));
            }
        }

        return exercises;
    }

    public TreeMap<String, ArrayList<TestResult>> runTests()
            throws InitializationError, NoTestsRemainException {
        TreeMap<String, ArrayList<TestResult>> result =
                new TreeMap<String, ArrayList<TestResult>>();

        TreeSet<String> exercises = listExercises();

        BlockJUnit4ClassRunner runner =
                new BlockJUnit4ClassRunner(this.testClass);

        for (String exercise : exercises) {
            ArrayList<TestResult> exercise_results =
                    new ArrayList<TestResult>();

            RunNotifier notifier = new RunNotifier();
            SandboxListener listener = new SandboxListener(exercise_results);
            notifier.addFirstListener(listener);

            result.put(exercise, exercise_results);

            ExerciseFilter filter =
                    new ExerciseFilter(exercise);
            runner.filter(filter);
            runner.run(notifier);
        }

        return result;
    }
}
