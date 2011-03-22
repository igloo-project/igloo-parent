package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.odlabs.wiquery.core.javascript.JsStatement;

public final class TipsyHelper {
	
	public static final JsStatement closeTipsyStatement() {
		return new JsStatement().$(null, ".tipsy").chain("detach");
	}

	public static final HeaderContributor getCloseOnLoadHeaderContributor() {
		return JavascriptPackageResource.getHeaderContribution(TipsyHelper.class, "tipsy-close.js");
	}
	
	private TipsyHelper() {
	}

}
