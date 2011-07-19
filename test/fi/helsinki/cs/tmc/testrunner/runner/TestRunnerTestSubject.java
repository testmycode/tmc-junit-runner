
package fi.helsinki.cs.tmc.testrunner.runner;

import org.junit.Ignore;
import org.junit.Test;
import fi.helsinki.cs.tmc.testrunner.annotation.Exercise;
import static org.junit.Assert.*;

@Ignore
public class TestRunnerTestSubject {
    @Test
    @Exercise("one two three")
    public void successfulTestCaseForOneTwoThree() {
    }

    @Test
    @Exercise("two")
    public void successfulTestCaseForTwo() {
        fail("too bad");
    }
}
