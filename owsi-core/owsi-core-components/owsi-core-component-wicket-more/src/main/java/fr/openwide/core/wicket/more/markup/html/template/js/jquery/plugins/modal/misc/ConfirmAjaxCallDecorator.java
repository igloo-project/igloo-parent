package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.misc;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;

public class ConfirmAjaxCallDecorator implements IAjaxCallDecorator {
	private static final long serialVersionUID = -7518492685202693675L;
	
	@Override
	public CharSequence decorateScript(Component component, CharSequence script) {
		Options options = new Options();
		options.put("onConfirm", JsScope.quickScope(script));
		return new JsStatement().$(component).chain("confirm", options.getJavaScriptOptions()).append(";")
				.append("return false;") // annulation de l'événément originel
				.render();
	}
	
	@Override
	public CharSequence decorateOnSuccessScript(Component component, CharSequence script) {
		return null;
	}
	
	@Override
	public CharSequence decorateOnFailureScript(Component component, CharSequence script) {
		return null;
	}
}
