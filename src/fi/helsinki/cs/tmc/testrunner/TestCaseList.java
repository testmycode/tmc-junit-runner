
package fi.helsinki.cs.tmc.testrunner;

import java.util.ArrayList;
import java.util.Arrays;

public class TestCaseList extends ArrayList<TestCase> {

    public TestCaseList findByMethodName(String methodName) {
        TestCaseList result = new TestCaseList();
        for (TestCase t : this) {
            if (t.methodName.equals(methodName)) {
                result.add(t);
            }
        }
        return result;
    }

    public TestCaseList findByPointName(String pointName) {
        TestCaseList result = new TestCaseList();
        for (TestCase t : this) {
            if (Arrays.asList(t.pointNames).contains(pointName)) {
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public TestCaseList clone() {
        TestCaseList clone = new TestCaseList();

        for (TestCase t : this) {
            clone.add(new TestCase(t));
        }

        return clone;
    }

}
