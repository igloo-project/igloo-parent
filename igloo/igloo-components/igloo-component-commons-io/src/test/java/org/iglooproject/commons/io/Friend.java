package org.iglooproject.commons.io;

import org.iglooproject.commons.io.internal.ClassPathResourceHelper;

/**
 * Allow package protected access to {@link ClassPathResourceUtil} constructor.
 */
public class Friend {

	public static ClassPathResourceUtil classPathResourceUtil(ClassLoader classLoader, ClassPathResourceHelper helper) {
		return new ClassPathResourceUtil(classLoader, helper);
	}

}
