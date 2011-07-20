
package fi.helsinki.cs.tmc.testrunner;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class TestRun implements Runnable {

    private TreeSet<String> exercises;
    private TreeMap<String, ArrayList<TestResult>> results;
    private Class testClass;

    public TestRun(Class testClass, TreeSet<String> exercises,
            TreeMap<String, ArrayList<TestResult>> results) {
        this.testClass = testClass;
        this.exercises = exercises;
        this.results = results;
    }

    public void run() {
        try {
            BlockJUnit4ClassRunner runner =
                    new BlockJUnit4ClassRunner(this.testClass);
            for (String exercise : exercises) {
                ArrayList<TestResult> exercise_results = new ArrayList<TestResult>();
                RunNotifier notifier = new RunNotifier();
                TestListener listener = new TestListener(exercise_results);
                notifier.addFirstListener(listener);
                results.put(exercise, exercise_results);
                ExerciseFilter filter = new ExerciseFilter(exercise);
                runner.filter(filter);
                runner.run(notifier);
            }
        } catch (NoTestsRemainException ex) {
        } catch (InitializationError ex) {
        }
    }
}
