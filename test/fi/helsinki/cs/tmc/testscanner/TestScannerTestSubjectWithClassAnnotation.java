package fi.helsinki.cs.tmc.testscanner;

import fi.helsinki.cs.tmc.testrunner.Points;
import org.junit.Test;

@Points("all")
public class TestScannerTestSubjectWithClassAnnotation {

    @Test
    public void bareTestMethod() {
    }

    public void notATestMethod() {
    }

    @Test
    @Points("one")
    public void oneExTestMethod() {
    }

    @Test
    @Points("one")
    public void secondOneExTestMethod() {
    }

    @Test
    @Points("one two")
    public void twoExTestMethod() {
    }
}
