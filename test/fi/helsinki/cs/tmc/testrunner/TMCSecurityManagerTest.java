package fi.helsinki.cs.tmc.testrunner;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/* ClassLoader setup doesn't work */
@Ignore
public class TMCSecurityManagerTest {

    private static URLClassLoader classLoader;
    private static Class loadedClass;
    private static Object instance;

    private static String origClassPath = "build/test/classes";
    private static String classPathLink = "testclasses";

    @BeforeClass
    public static void setUpClass() throws Exception {
        Process p = Runtime.getRuntime().exec("ln " + origClassPath +
                " " + classPathLink);

        System.out.println("linked");
        Thread.sleep(5000);
        String policyFile = "testrunner.policy";
        String testClassName =
                "fi.helsinki.cs.tmc.testrunner.TMCSecurityManagerTestSubject";
        TMCSecurityManager.setupSecurityManager(classPathLink, policyFile);

        URL[] urls = { new File(classPathLink).toURI().toURL() };
        classLoader = new URLClassLoader(urls);
        loadedClass = classLoader.loadClass(testClassName);
        instance = loadedClass.newInstance();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        classLoader = null;
        loadedClass = null;
        instance = null;
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected=SecurityException.class)
    public void testIllegalFileCreation() throws Exception {
        Method m = loadedClass.getDeclaredMethod("illegalFileCreation");
        m.invoke(instance);
    }
}