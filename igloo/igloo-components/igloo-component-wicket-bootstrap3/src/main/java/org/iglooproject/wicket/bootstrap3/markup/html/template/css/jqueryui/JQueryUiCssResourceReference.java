package org.iglooproject.wicket.bootstrap3.markup.html.template.css.jqueryui;

import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;

public final class JQueryUiCssResourceReference extends WebjarsCssResourceReference {

	private static final long serialVersionUID = -3914656103299973526L;

	private static final JQueryUiCssResourceReference INSTANCE = new JQueryUiCssResourceReference();

	private JQueryUiCssResourceReference() {
		super("jquery-ui/current/themes/base/all.css");
	}

	public static JQueryUiCssResourceReference get() {
		return INSTANCE;
	}

}
