package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.modal;

import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;

/**
 * @see ModalJavaScriptResourceReference
 */
@Deprecated
public final class ModalStyleSheetResourceReference extends WebjarsCssResourceReference {
	private static final long serialVersionUID = -9209473494619975852L;
	
	private static final ModalStyleSheetResourceReference INSTANCE = new ModalStyleSheetResourceReference();

	private ModalStyleSheetResourceReference() {
		super("jquery.fancybox/current/jquery.fancybox-1.3.5-ow.css");
	}
	
	public static ModalStyleSheetResourceReference get() {
		return INSTANCE;
	}

}
