package fi.helsinki.cs.tmc.testrunner.runner;

import fi.helsinki.cs.tmc.testrunner.annotation.Exercise;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Test;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class RunnerThread extends Thread {

    private Class testClass;
    private ArrayList<String> exercises;
    private TreeMap<String, ArrayList<TestResult>> results;

    public RunnerThread(Class testClass,
            TreeMap<String, ArrayList<TestResult>> results) {
        this.testClass = testClass;
        this.results = results;
    }

    private ArrayList<String> listExercises()
{
        TreeSet<String> exerciseSet = new TreeSet<String>();

        for (Method m : this.testClass.getMethods()) {
            Test t = m.getAnnotation(Test.class);
            if (t == null) {
                continue;
            }

            Exercise e = m.getAnnotation(Exercise.class);
            if (e != null) {
                exerciseSet.add(e.value());
            }
        }

        return new ArrayList<String>(exerciseSet);
    }

    @Override
    public void run() {
        this.exercises = listExercises();
        try {
            runTests();
        } catch (InitializationError ex) {

        } catch (NoTestsRemainException ex) {
        }

    }

    private void runTests() throws InitializationError, NoTestsRemainException {
        BlockJUnit4ClassRunner runner =
                new BlockJUnit4ClassRunner(this.testClass);

        for (String exercise : exercises) {
            ArrayList<TestResult> exercise_results =
                    new ArrayList<TestResult>();

            RunNotifier notifier = new RunNotifier();
            TestListener listener = new TestListener(exercise_results);
            notifier.addFirstListener(listener);

            results.put(exercise, exercise_results);

            ExerciseFilter filter =
                    new ExerciseFilter(exercise);
            runner.filter(filter);
            runner.run(notifier);
        }
    }
}
