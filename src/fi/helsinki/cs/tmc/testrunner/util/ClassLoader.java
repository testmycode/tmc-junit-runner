package fi.helsinki.cs.tmc.testrunner.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoader {

	public static Class loadClass(String classPath, String className)
		throws MalformedURLException, ClassNotFoundException
	{
                File myFile = new File(classPath);
                URL[] urls = { myFile.toURI().toURL() };
                URLClassLoader cl = new URLClassLoader(urls);
		return cl.loadClass(className);
	}
}
