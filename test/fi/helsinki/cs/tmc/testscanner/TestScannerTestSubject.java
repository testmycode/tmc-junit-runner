package fi.helsinki.cs.tmc.testscanner;

import fi.helsinki.cs.tmc.testrunner.Exercise;
import org.junit.Test;

public class TestScannerTestSubject {
	@Test
	public void bareTestMethod() {
	}
	
	public void notATestMethod() {
	}
	
	@Test
	@Exercise("one")
	public void oneExTestMethod() {
	}
	
	@Test
	@Exercise("one")
	public void secondOneExTestMethod() {
	}
	
	@Test
	@Exercise("one two")
	public void twoExTestMethod() {
	}
}
