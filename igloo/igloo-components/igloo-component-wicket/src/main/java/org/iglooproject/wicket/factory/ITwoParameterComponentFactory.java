package org.iglooproject.wicket.factory;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;

@FunctionalInterface
public interface ITwoParameterComponentFactory<C extends Component, P1, P2> extends IDetachable {

	C create(String wicketId, P1 parameter1, P2 parameter2);

	@Override
	default void detach() {
	}

}
