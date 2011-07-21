package fi.helsinki.cs.tmc.testrunner;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TimeoutTestSubject {
    @Test
    @Exercise("infinite")
    public void infinite() {
        for (;;) {}
    }
}
