package fi.helsinki.cs.tmc.testrunner.runner;

import java.security.Permission;

public class TMCSecurityManager extends SecurityManager {

	@Override
	public void checkRead(String fileName) {
	}
	
	@Override
	public void checkWrite(String fileName) {
		if (isUntrusted())
			throw new SecurityException("checkWrite");
	}
	
	@Override
	public void checkDelete(String fileName) {
		if (isUntrusted())
			throw new SecurityException("checkDelete");
	}

	@Override
	public void checkPermission(Permission perm)
	{
		if (isUntrusted())
			throw new SecurityException("checkPermission");
	}


	private boolean isUntrusted()
	{
		ClassLoader systemCL = ClassLoader.getSystemClassLoader();
		Class c[] = getClassContext();
		for (int i = 1; i < c.length; i++)
		{
			ClassLoader cl = c[i].getClassLoader();
			if (!(cl == null || cl == systemCL))
				return true;
		}
		return false;
	}

}
