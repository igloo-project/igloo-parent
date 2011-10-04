package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

/**
 * Affiche un composant arbitraire de la page dans une fancybox
 * (appel du type $.fancybox('', { href: '#anchor' });
 */
public class FancyboxAnchor extends DefaultTipsyFancybox {

	private static final long serialVersionUID = 6098428778089977967L;
	
	private Component component;
	
	public FancyboxAnchor(Component component) {
		this.component = component;
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		options.put("href", JsUtils.quotes("#" + component.getMarkupId(), true));
		return new CharSequence[] { JsUtils.quotes(""), options.getJavaScriptOptions() };
	}

}
