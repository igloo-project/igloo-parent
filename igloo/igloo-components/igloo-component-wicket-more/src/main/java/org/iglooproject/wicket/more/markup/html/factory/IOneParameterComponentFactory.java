package org.iglooproject.wicket.more.markup.html.factory;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;

@FunctionalInterface
public interface IOneParameterComponentFactory<C extends Component, P> extends IDetachable {

	C create(String wicketId, P parameter);

	@Override
	default void detach() {
	}

}
