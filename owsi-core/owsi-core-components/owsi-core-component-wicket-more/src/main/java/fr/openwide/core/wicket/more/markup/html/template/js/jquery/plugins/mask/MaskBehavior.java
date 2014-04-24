package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.mask;

import org.apache.http.util.Args;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.JQueryAbstractBehavior;

public class MaskBehavior extends JQueryAbstractBehavior {
	
	private static final long serialVersionUID = 942354041972092047L;
	
	private static final String MASK = "mask";
	
	private final String maskPattern;
	
	public MaskBehavior(String maskPattern) {
		Args.notBlank(maskPattern, "maskPattern");
		this.maskPattern = maskPattern;
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(MaskJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement().render()));
	}
	
	public JsStatement statement() {
		return new JsStatement().$(getComponent()).chain(MASK, JsUtils.quotes(maskPattern));
	}
}
