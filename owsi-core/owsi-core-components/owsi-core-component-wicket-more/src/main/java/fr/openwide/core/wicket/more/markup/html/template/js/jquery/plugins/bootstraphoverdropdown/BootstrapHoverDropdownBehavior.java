package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstraphoverdropdown;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.JQueryAbstractBehavior;

public class BootstrapHoverDropdownBehavior extends JQueryAbstractBehavior {

	private static final long serialVersionUID = -2996190309864839178L;

	private static final String DROPDOWN_HOVER = "dropdownHover";

	public static final String DEFAULT_SELECTOR = "[data-hover=\"dropdown\"]";

	private final String selector;

	private final BootstrapHoverDropdownOptions options;

	public BootstrapHoverDropdownBehavior() {
		this(DEFAULT_SELECTOR, new BootstrapHoverDropdownOptions());
	}

	public BootstrapHoverDropdownBehavior(BootstrapHoverDropdownOptions options) {
		this(DEFAULT_SELECTOR, options);
	}

	public BootstrapHoverDropdownBehavior(String selector, BootstrapHoverDropdownOptions options) {
		super();
		this.selector = selector;
		this.options = options;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapHoverDropdownJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement().render()));
	}

	public JsStatement statement() {
		return new JsStatement().$(getComponent(), selector).chain(DROPDOWN_HOVER, options.getJavaScriptOptions());
	}

}
