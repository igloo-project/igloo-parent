package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.feedback;

import igloo.jquery.util.AbstractCoreJQueryPluginResourceReference;

public final class FeedbackJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -1811004494719333505L;

	private static final FeedbackJavaScriptResourceReference INSTANCE = new FeedbackJavaScriptResourceReference();

	private FeedbackJavaScriptResourceReference() {
		super(FeedbackJavaScriptResourceReference.class, "jquery.feedback.js");
	}

	public static FeedbackJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
