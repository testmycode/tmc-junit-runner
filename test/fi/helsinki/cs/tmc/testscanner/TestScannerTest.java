package fi.helsinki.cs.tmc.testscanner;

import java.util.HashMap;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestScannerTest {

    @Test
    public void shouldFindAllTestClassesAndMethodsUnderGivenDirectory() throws Exception {
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        try {
            System.setOut(new PrintStream(outBuf, true, "UTF-8"));
            String[] args = {
                "test"
            };
            TestScanner.main(args);
        } finally {
            System.setOut(oldOut);
        }

        String output = outBuf.toString("UTF-8");
        TestMethod[] outData = new Gson().fromJson(output, TestMethod[].class);
        assertNotNull(outData);

        String testSubjectName = "TestScannerTestSubject";
        HashMap<String, String[]> methodsFound = new HashMap<String, String[]>();
        for (TestMethod m : outData) {
            if (m.className.equals(testSubjectName)) {
                methodsFound.put(m.methodName, m.exercises);
            }
        }

        assertArrayEquals(arr("one"), methodsFound.get("oneExTestMethod"));
        assertArrayEquals(arr("one"), methodsFound.get("secondOneExTestMethod"));
        assertArrayEquals(arr("one", "two"), methodsFound.get("twoExTestMethod"));

        assertArrayEquals(arr(), methodsFound.get("bareTestMethod"));

        assertFalse(methodsFound.containsKey("notATestMethod"));
    }

    private static Object[] arr(Object... a) {
        return a;
    }
}
