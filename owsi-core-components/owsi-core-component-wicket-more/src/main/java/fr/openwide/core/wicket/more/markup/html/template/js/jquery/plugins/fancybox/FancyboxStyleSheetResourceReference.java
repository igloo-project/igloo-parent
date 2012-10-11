package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.request.resource.CssResourceReference;


public class FancyboxStyleSheetResourceReference extends CssResourceReference {

	private static final long serialVersionUID = -9209473494619975852L;

	private static final FancyboxStyleSheetResourceReference INSTANCE = new FancyboxStyleSheetResourceReference();

	private FancyboxStyleSheetResourceReference() {
		super(FancyboxStyleSheetResourceReference.class, "jquery.fancybox-1.3.4/fancybox/jquery.fancybox-modified-1.3.4.css");
	}

	public static FancyboxStyleSheetResourceReference get() {
		return INSTANCE;
	}

}
