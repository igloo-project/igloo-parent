package org.iglooproject.basicapp.web.application.common.util;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.wicketstuff.wiquery.core.javascript.JsQuery;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

public final class BootstrapTabsUtils {

	public static final String ANCHOR_ROOT = "#";

	public static WebMarkupContainer buildTabLink(String id, String anchor) {
		WebMarkupContainer tabLink = new WebMarkupContainer(id);
		return decorateTabLink(tabLink, anchor);
	}
	
	public static WebMarkupContainer decorateTabLink(WebMarkupContainer tabLink, String anchor) {
		tabLink.add(new AttributeModifier("href", ANCHOR_ROOT + anchor));
		return tabLink;
	}

	public static JsStatement show(Component context) {
		return new JsStatement().$(context).chain("tab", JsUtils.quotes("show"));
	}

	public static JsStatement show(String tab) {
		return new JsQuery().$("a[href=\"#" + tab + "\"]").chain("tab", JsUtils.quotes("show"));
	}

	private BootstrapTabsUtils() {
	}

}
