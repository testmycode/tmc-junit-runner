
package fi.helsinki.cs.tmc.testrunner;

import fi.helsinki.cs.tmc.testrunner.util.ClassLoader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassLoaderTest {

	@Test
	public void testLoadClass() throws Exception {
		String classPath = "build/test/classes";
		String className =
			"fi.helsinki.cs.tmc.testrunner.testclasses.MyString";
		Class result = ClassLoader.loadClass(classPath, className);
		assertNotNull(result);

		ArrayList<String> foundMethodNames = new ArrayList<String>();

		for (Method m : result.getMethods())
		{
			foundMethodNames.add(m.getName());
		}

		String[] expMethodNames = {
			"length", "charAt", "toString"
		};

		for (String methodName : expMethodNames)
		{
			assertTrue(foundMethodNames.contains(methodName));
		}
		assertFalse(foundMethodNames.contains("nonExistantMethod"));
	}

}