
package fi.helsinki.cs.tmc.testrunner;

import java.util.ArrayList;
import java.util.Arrays;

public class TestCases extends ArrayList<TestCase> {

    public TestCases findByMethodName(String methodName) {
        TestCases result = new TestCases();
        for (TestCase t : this) {
            if (t.methodName.equals(methodName)) {
                result.add(t);
            }
        }
        return result;
    }

    public TestCases findByPointName(String pointName) {
        TestCases result = new TestCases();
        for (TestCase t : this) {
            if (Arrays.asList(t.pointNames).contains(pointName)) {
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public TestCases clone() {
        TestCases clone = new TestCases();

        for (TestCase t : this) {
            clone.add(new TestCase(t));
        }

        return clone;
    }

}
