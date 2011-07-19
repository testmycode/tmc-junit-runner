
package fi.helsinki.cs.tmc.testrunner;

import org.junit.Ignore;
import org.junit.Test;
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
