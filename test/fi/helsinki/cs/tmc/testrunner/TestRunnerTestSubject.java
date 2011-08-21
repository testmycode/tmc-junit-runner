
package fi.helsinki.cs.tmc.testrunner;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

@Ignore
public class TestRunnerTestSubject {
    @Test
    public void successfulTestCaseForOneTwoThree() {
    }

    @Test
    public void failingTestCaseForTwo() {
        fail("too bad");
    }
}

