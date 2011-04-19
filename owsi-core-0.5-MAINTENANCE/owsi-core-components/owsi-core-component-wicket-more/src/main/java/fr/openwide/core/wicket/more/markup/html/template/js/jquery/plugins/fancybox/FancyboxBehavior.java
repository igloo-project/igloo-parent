package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.commons.CoreJavaScriptResourceReference;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;

public class FancyboxBehavior extends WiQueryAbstractBehavior{
	private static final long serialVersionUID = 7556130610172681270L;
	
	private DefaultTipsyFancybox fancybox;
	
	private String selector;
	
	public FancyboxBehavior(String selector, DefaultTipsyFancybox fancybox) {
		super();
		this.fancybox = fancybox;
		this.selector = selector;
	}

	@Override
	public JsStatement statement() {
		return new JsStatement().$(getComponent(), selector).chain(fancybox);
	}
	
	@Override
	public void contribute(WiQueryResourceManager wiQueryResourceManager) {
		wiQueryResourceManager.addJavaScriptResource(CoreJavaScriptResourceReference.get());
		wiQueryResourceManager.addJavaScriptResource(EasingJavaScriptResourceReference.get());
		wiQueryResourceManager.addJavaScriptResource(FancyboxJavaScriptResourceReference.get());
		wiQueryResourceManager.addCssResource(FancyboxStyleSheetResourceReference.get());
		super.contribute(wiQueryResourceManager);
	}

	@Override
	public void bind(Component component) {
		component.setOutputMarkupId(true);
		super.bind(component);
	}

}
