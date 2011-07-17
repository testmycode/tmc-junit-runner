package fi.helsinki.cs.tmc.testrunner;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

public class ExerciseFilter extends Filter {

    private String exercise;

    public ExerciseFilter(String exercise) {
        this.exercise = exercise;
    }

    @Override
    public String describe() {
        return "Filters tests based on exercise";
    }

    @Override
    public boolean shouldRun(Description description) {
        Exercise annotation = description.getAnnotation(Exercise.class);
        if (annotation != null) {
            String[] values = annotation.value().split(" +");
            for (String value : values) {
                if (value.equals(this.exercise)) {
                    return true;
                }
            }
        }
        return false;
    }
}
