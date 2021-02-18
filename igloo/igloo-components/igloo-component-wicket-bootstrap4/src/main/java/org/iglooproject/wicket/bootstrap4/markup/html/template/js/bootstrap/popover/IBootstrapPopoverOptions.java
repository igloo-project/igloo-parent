package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.popover;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.IStatementValueOption;

public interface IBootstrapPopoverOptions extends IDetachable {

	CharSequence getJavaScriptOptions(Component component);

	interface IPlacement extends IStatementValueOption {
	}

	interface ITrigger extends IStatementValueOption {
	}

}
