package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.util;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

public final class AjaxConfirmUtil {

	public static JsStatement getTriggerEventOnConfirmStatement(Component component, String event) {
		Options options = new Options();
		options.put("onConfirm", JsScope.quickScope(new JsStatement().$(component).chain("trigger", JsUtils.quotes(event))));
		
		return new JsStatement().$(component)
				.chain("confirm", options.getJavaScriptOptions()).append(";")
				.append("return false;");
	}

	private AjaxConfirmUtil() {
	}
}
