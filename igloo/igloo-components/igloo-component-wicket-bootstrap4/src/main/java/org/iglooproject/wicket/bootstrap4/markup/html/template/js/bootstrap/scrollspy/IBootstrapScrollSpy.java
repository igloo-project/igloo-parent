package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.scrollspy;

import org.iglooproject.wicket.bootstrap4.markup.html.template.js.IDetachableChainableStatement;

public interface IBootstrapScrollSpy extends IDetachableChainableStatement {

	static final String CHAIN_LABEL = "scrollspy";

	@Override
	default String chainLabel() {
		return CHAIN_LABEL;
	}

	@Override
	default void detach() {
		// nothing to do
	}

}
