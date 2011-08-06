package fi.helsinki.cs.tmc.testscanner;

import java.util.Arrays;

/**
 * A test method that is required to pass zero or more exercises.
 */
public class TestMethod {

    public String className;
    public String methodName;
    public String[] points;

    public TestMethod(String className, String methodName, String[] exercises) {
        this.className = className;
        this.methodName = methodName;
        this.points = exercises;
    }

    @Override
    public String toString() {
        return className + "." + methodName + ":" + Arrays.toString(points);
    }
}
