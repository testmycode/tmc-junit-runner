
package fi.helsinki.cs.tmc.testrunner;

import java.io.FileReader;

public class TMCSecurityManagerTestSubject {
    public void illegalFileRead() throws Exception {
        FileReader reader = new FileReader("/tmp/illegal");
        int c = reader.read();
    }

    public void illegalSetSecurityManager() {
        System.setSecurityManager(null);
    }
}
