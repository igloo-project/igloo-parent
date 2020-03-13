package org.iglooproject.wicket.more.markup.html.bootstrap.common.model;

public enum BootstrapColor implements IBootstrapColor {

	/**
	 * Bootstrap 3 default color. Use {@link BootstrapColor#SECONDARY} in Bootstrap 4.
	 */
	@Deprecated
	DEFAULT("default"),
	BRAND("brand"),
	PRIMARY("primary"),
	/**
	 * Bootstrap 4 default color. Use {@link BootstrapColor#DEFAULT} in Bootstrap 3.
	 */
	SECONDARY("secondary"),
	SUCCESS("success"),
	INFO("info"),
	TODO("todo"),
	WARNING("warning"),
	@Deprecated
	WARNING_SEVERE("warning"),
	DANGER("danger");

	private final String cssClassSuffix;

	private BootstrapColor(String cssClassSuffix) {
		this.cssClassSuffix = cssClassSuffix;
	}

	@Override
	public String getCssClassSuffix() {
		return cssClassSuffix;
	}

}
