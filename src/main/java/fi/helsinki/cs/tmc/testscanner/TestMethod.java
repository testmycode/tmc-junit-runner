package fi.helsinki.cs.tmc.testscanner;

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
        StringBuilder result = new StringBuilder();
        result.append(className).append('.').append(methodName).append('{');
        if (points.length > 0) {
            for (int i = 0; i < points.length - 1; ++i) {
                result.append(points[i]).append(',');
            }
            result.append(points[points.length-1]);
        }
        result.append('}');
        return result.toString();
    }
}
