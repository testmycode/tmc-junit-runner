package fi.helsinki.cs.tmc.testrunner;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TimeoutTestSubject {

    @Test
    @Points("infinite")
    public void infinite() {
        for (;;) {}
    }

    @Test
    @Points("passing")
    public void empty() {
    }

    @Test
    @Points("passing")
    public void empty2() {
    }
}
