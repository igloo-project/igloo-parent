package test.web.property;

import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.ImmutablePropertyId;

public class SeleniumPropertyIds extends AbstractPropertyIds {

	private SeleniumPropertyIds() {
	}

	/*
	 * Immutable Properties
	 */
	
	public static final ImmutablePropertyId<String> GECKODRIVER_PATH = immutable("selenium.firefox.geckodriver.path");
	public static final ImmutablePropertyId<String> XVFB_DISPLAY = immutable("selenium.xvfb.display");
}
