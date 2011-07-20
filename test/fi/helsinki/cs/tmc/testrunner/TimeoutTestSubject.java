package fi.helsinki.cs.tmc.testrunner;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TimeoutTestSubject {
    @Test
    @Exercise("one")
    public void succesfulOne() {}

    @Test
    @Exercise("two")
    public void infiniteTwo() {
        for (;;) {}
    }
}
