package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tooltip;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.IDetachableChainableStatement;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.IStatementValueOption;

public interface IBootstrapTooltip extends IDetachableChainableStatement {

	static final String CHAIN_LABEL = "tooltip";

	IModel<? extends CharSequence> getSelectorModel();

	@Override
	default String chainLabel() {
		return CHAIN_LABEL;
	}

	@Override
	default void detach() {
		// nothing to do
	}

	interface IPlacement extends IStatementValueOption {
	}

	interface ITrigger extends IStatementValueOption {
	}

}
