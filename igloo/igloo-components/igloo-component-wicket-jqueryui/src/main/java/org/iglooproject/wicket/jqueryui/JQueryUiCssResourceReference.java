package org.iglooproject.wicket.jqueryui;

import org.apache.wicket.request.resource.CssResourceReference;

public final class JQueryUiCssResourceReference extends CssResourceReference {

	private static final long serialVersionUID = -3914656103299973526L;

	private static final JQueryUiCssResourceReference INSTANCE = new JQueryUiCssResourceReference();

	private JQueryUiCssResourceReference() {
		super(JQueryUiCssResourceReference.class, "jquery-ui.css");
	}

	public static JQueryUiCssResourceReference get() {
		return INSTANCE;
	}

}
