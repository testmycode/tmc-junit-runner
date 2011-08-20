package fi.helsinki.cs.tmc.testscanner;

import java.util.HashMap;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestScannerTest {

    @Test
    public void shouldReturnAllTestMethodsInADirectoryWithThePointsAnnotation() throws Exception {
        TestMethod[] outData = invokeTestScanner();

        HashMap<String, String[]> methodPoints = getMethodsToPointsMap(outData, "TestScannerTestSubject");

        assertArrayEquals(arr("one"), methodPoints.get("oneExTestMethod"));
        assertArrayEquals(arr("one"), methodPoints.get("secondOneExTestMethod"));
        assertArrayEquals(arr("one", "two"), methodPoints.get("twoExTestMethod"));

        assertArrayEquals(null, methodPoints.get("bareTestMethod"));

        assertFalse(methodPoints.containsKey("notATestMethod"));
    }
    
    @Test
    public void shouldIncludeClassPointsAnnotationInEachMethod() throws Exception {
        TestMethod[] outData = invokeTestScanner();

        HashMap<String, String[]> methodPoints = getMethodsToPointsMap(outData, "TestScannerTestSubjectWithClassAnnotation");

        assertArrayEquals(arr("all", "one"), methodPoints.get("oneExTestMethod"));
        assertArrayEquals(arr("all", "one"), methodPoints.get("secondOneExTestMethod"));
        assertArrayEquals(arr("all", "one", "two"), methodPoints.get("twoExTestMethod"));

        assertArrayEquals(arr("all"), methodPoints.get("bareTestMethod"));

        assertFalse(methodPoints.containsKey("notATestMethod"));
    }

    private HashMap<String, String[]> getMethodsToPointsMap(TestMethod[] methods, String className) {
        HashMap<String, String[]> methodPoints = new HashMap<String, String[]>();
        for (TestMethod m : methods) {
            if (m.className.equals(className)) {
                methodPoints.put(m.methodName, m.points);
            }
        }
        return methodPoints;
    }
    
    private TestMethod[] invokeTestScanner() throws Exception {
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
        return outData;
    }

    private static Object[] arr(Object... a) {
        return a;
    }
}
