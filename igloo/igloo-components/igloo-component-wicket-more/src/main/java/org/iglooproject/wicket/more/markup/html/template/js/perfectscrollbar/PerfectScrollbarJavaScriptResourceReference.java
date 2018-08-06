package org.iglooproject.wicket.more.markup.html.template.js.perfectscrollbar;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class PerfectScrollbarJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = 1762476460042247594L;

	private static final PerfectScrollbarJavaScriptResourceReference INSTANCE = new PerfectScrollbarJavaScriptResourceReference();

	private PerfectScrollbarJavaScriptResourceReference() {
		super("perfect-scrollbar/current/dist/perfect-scrollbar.js");
	}

	public static PerfectScrollbarJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
