package org.iglooproject.wicket.bootstrap4.markup.html.template.js;

import org.apache.wicket.model.IDetachable;
import org.wicketstuff.wiquery.core.javascript.ChainableStatement;

public interface IDetachableChainableStatement extends ChainableStatement, IDetachable {

	@Override
	default void detach() {
		// nothing to do
	}

}
