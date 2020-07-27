package org.iglooproject.wicket.more.markup.html.factory;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;

@FunctionalInterface
public interface IComponentFactory<C extends Component> extends IDetachable {

	C create(String wicketId);

	@Override
	default void detach() {
		// nothing to do
	}

}
