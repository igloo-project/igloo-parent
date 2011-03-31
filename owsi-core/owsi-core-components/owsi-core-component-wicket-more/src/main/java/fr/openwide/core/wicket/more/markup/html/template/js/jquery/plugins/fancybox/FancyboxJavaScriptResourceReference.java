package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.odlabs.wiquery.core.commons.compressed.WiQueryYUICompressedJavaScriptResourceReference;

public class FancyboxJavaScriptResourceReference extends WiQueryYUICompressedJavaScriptResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;
	
	private static final FancyboxJavaScriptResourceReference INSTANCE = new FancyboxJavaScriptResourceReference();

	public FancyboxJavaScriptResourceReference() {
		super(FancyboxJavaScriptResourceReference.class, "jquery.fancybox-1.3.4/fancybox/jquery.fancybox-1.3.4.js");
	}
	
	public static FancyboxJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
