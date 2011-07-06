package testrunner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mrannanj
 */
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value= ElementType.METHOD)
public @interface Exercise {
	public String value();
}
