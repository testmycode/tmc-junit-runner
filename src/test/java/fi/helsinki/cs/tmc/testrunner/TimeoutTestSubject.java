package fi.helsinki.cs.tmc.testrunner;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TimeoutTestSubject {

    @Test
    public void infinite() {
        for (;;) {}
    }

    @Test
    public void empty() {
    }

    @Test
    public void empty2() {
    }
}
