package org.iglooproject.wicket.bootstrap4.markup.html.template.css.jqueryui;

import org.apache.wicket.request.resource.CssResourceReference;

public final class JQueryUiCssResourceReference extends CssResourceReference {

	private static final long serialVersionUID = -4192693085575758769L;

	private static final JQueryUiCssResourceReference INSTANCE = new JQueryUiCssResourceReference();

	private JQueryUiCssResourceReference() {
		super(JQueryUiCssResourceReference.class, "jquery-ui.css");
	}

	public static JQueryUiCssResourceReference get() {
		return INSTANCE;
	}

}
