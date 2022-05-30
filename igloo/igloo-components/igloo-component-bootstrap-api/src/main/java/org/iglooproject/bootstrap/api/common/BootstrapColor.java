package org.iglooproject.bootstrap.api.common;

public enum BootstrapColor implements IBootstrapColor {

	BRAND("brand"),
	PRIMARY("primary"),
	SECONDARY("secondary"),
	SUCCESS("success"),
	INFO("info"),
	TODO("todo"),
	WARNING("warning"),
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
