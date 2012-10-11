package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.elastic;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class ElasticJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -467593019533386890L;

	private static final ElasticJavaScriptResourceReference INSTANCE = new ElasticJavaScriptResourceReference();

	private ElasticJavaScriptResourceReference() {
		super(ElasticJavaScriptResourceReference.class, "jquery.elastic.source.js");
	}

	public static ElasticJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
