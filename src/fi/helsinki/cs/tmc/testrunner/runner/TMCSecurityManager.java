package fi.helsinki.cs.tmc.testrunner.runner;

import java.io.File;
import java.security.Permission;
import java.security.Policy;
import java.util.ArrayList;

public class TMCSecurityManager extends SecurityManager
{
    private ArrayList<String> classPaths = new ArrayList<String>();

    public static void setupSM(String classPath, String policyFilePath) {
        String testClassPath = new File(classPath).getAbsolutePath();
        System.setProperty("java.security.policy", policyFilePath);
        System.setProperty("testclasspath", testClassPath);
        Policy.getPolicy().refresh();
        System.setSecurityManager(new TMCSecurityManager());
    }

    public TMCSecurityManager() {
        buildClassPaths();
    }

    @Override
    public void checkPermission(Permission perm) {
        if (isTrusted()) {
            return;
        }
        super.checkPermission(perm);
    }

    @Override
    public void checkRead(String filename) {
        if (inClassPath(filename)) {
            return;
        }
        super.checkRead(filename);
    }

    private boolean inClassPath(String filename) {
        for (String classPath : this.classPaths) {
            if (filename.startsWith(classPath)) {
                return true;
            }
        }
        return false;
    }

    private void buildClassPaths() {
        String relativePaths[] =
                System.getProperty("java.class.path").
                split(System.getProperty("path.separator"));

        for (String relativePath : relativePaths) {
            this.classPaths.add(
                    new File(relativePath).getAbsolutePath());
        }

    }

    private boolean isTrusted() {
        ClassLoader systemCL = ClassLoader.getSystemClassLoader();
        Class c[] = getClassContext();
        for (int i = 1; i < c.length; i++) {
            ClassLoader cl = c[i].getClassLoader();
            if (cl != null && cl != systemCL) {
                return false;
            }
        }
        return true;
    }
}
