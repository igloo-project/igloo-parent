package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy;

import org.apache.wicket.request.resource.PackageResourceReference;

public class TipsyCloseOnLoadJavascriptResourceReference extends PackageResourceReference {

	private static final long serialVersionUID = -1315223031352762415L;

	private static final TipsyCloseOnLoadJavascriptResourceReference INSTANCE = new TipsyCloseOnLoadJavascriptResourceReference();

	public TipsyCloseOnLoadJavascriptResourceReference() {
		super(TipsyCloseOnLoadJavascriptResourceReference.class, "tipsy-close.js");
	}

	public static final TipsyCloseOnLoadJavascriptResourceReference get() {
		return INSTANCE;
	}

}
