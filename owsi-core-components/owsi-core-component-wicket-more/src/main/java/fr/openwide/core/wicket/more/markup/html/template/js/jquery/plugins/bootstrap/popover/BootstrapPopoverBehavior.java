package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeContext;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class BootstrapPopoverBehavior extends Behavior {

	private static final long serialVersionUID = -4381194681563091269L;

	private static final String BOOTSTRAP_POPOVER = "popover";

	private BootstrapPopoverOptions options;

	public BootstrapPopoverBehavior(BootstrapPopoverOptions options) {
		super();
		this.options = options;
	}

	public JsStatement statement(Component component) {
		JsStatement popoverStatement = new JsStatement().$(component).chain(BOOTSTRAP_POPOVER, options.getJavaScriptOptions());
		if (PopoverTrigger.MANUAL.equals(options.getTrigger())) {
			JsScope clickFunction = new JsScope("e") {
				private static final long serialVersionUID = 1L;
				@Override
				protected void execute(JsScopeContext scopeContext) {
					scopeContext.append("$(this).popover('toggle')");
					scopeContext.append("e.preventDefault()");
				}
			};
			return popoverStatement.chain("click", clickFunction.render());
		} else {
			return popoverStatement;
		}
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapPopoverJavascriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement(component).render()));
	}

}
