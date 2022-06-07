package org.iglooproject.wicket.application;

import org.apache.wicket.Application;

public class CoreApplication {

	private CoreApplication() {}

	public static final ICoreApplication get() {
		return (ICoreApplication) Application.get();
	}

}
