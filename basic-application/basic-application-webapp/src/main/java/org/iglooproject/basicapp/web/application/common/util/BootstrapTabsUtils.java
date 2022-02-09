package org.iglooproject.basicapp.web.application.common.util;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.wicketstuff.wiquery.core.javascript.JsQuery;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

public final class BootstrapTabsUtils {

	public static String getTabMarkupId(String markupId) {
		return markupId + "-tab";
	}

	public static Component[] build(String markupId, Component tab, Component panel, IModel<Boolean> activeModel) {
		return build(getTabMarkupId(markupId), markupId, tab, panel, activeModel);
	}

	public static Component[] build(String tabMarkupId, String panelMarkupId, Component tab, Component panel, IModel<Boolean> activeModel) {
		tab.setMarkupId(tabMarkupId);
		panel.setMarkupId(panelMarkupId);
		
		tab.add(new ClassAttributeAppender(Condition.isTrue(activeModel).then("active").otherwise("")));
		tab.add(new AttributeModifier("data-bs-toggle", "tab"));
		tab.add(new AttributeModifier("data-bs-target", () -> "#" + panel.getMarkupId()));
		tab.add(new AttributeModifier("role", "tab"));
		tab.add(new AttributeModifier("aria-controls", panel::getMarkupId));
		tab.add(new AttributeModifier("aria-selected", activeModel));
		
		panel.add(new ClassAttributeAppender(Condition.isTrue(activeModel).then("active show").otherwise("")));
		panel.add(new AttributeModifier("role", "tabpanel"));
		panel.add(new AttributeModifier("aria-labelledby", tab::getMarkupId));
		
		return new Component[] { tab, panel };
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
