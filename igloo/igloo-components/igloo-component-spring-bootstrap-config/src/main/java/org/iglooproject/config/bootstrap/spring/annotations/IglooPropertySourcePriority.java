package org.iglooproject.config.bootstrap.spring.annotations;

public final class IglooPropertySourcePriority {

	/**
	 * Level dedicated to your application defaults.
	 */
	public static final String APPLICATION = "igloo/application";
	/**
	 * Level dedicated to Igloo framework defaults.
	 */
	public static final String FRAMEWORK = "igloo/framework";
	/**
	 * Level dedicated to component defaults.
	 */
	public static final String COMPONENT = "igloo/component";

	private IglooPropertySourcePriority() {};

}
