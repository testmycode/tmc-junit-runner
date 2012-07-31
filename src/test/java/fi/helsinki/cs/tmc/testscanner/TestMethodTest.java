package fi.helsinki.cs.tmc.testscanner;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestMethodTest {
    @Test
    public void testToString() {
        TestMethod tm;
        tm = new TestMethod("Cls", "method", new String[] {"1.1", "1.2", "1.3"});
        assertEquals("Cls.method{1.1,1.2,1.3}", tm.toString());
        
        tm = new TestMethod("Cls", "method", new String[] {"1.1"});
        assertEquals("Cls.method{1.1}", tm.toString());
        
        tm = new TestMethod("Cls", "method", new String[] {});
        assertEquals("Cls.method{}", tm.toString());
    }
}
