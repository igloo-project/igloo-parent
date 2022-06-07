package org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar.state;

import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.condition.Condition;

public interface IAddedToolbarCoreElementState<T, S extends ISort<?>> extends IAddedToolbarElementState<T, S> {

	@Override
	IAddedToolbarCoreElementState<T, S> when(Condition condition);

	@Override
	IAddedToolbarCoreElementState<T, S> withClass(String cssClass);

	@Override
	IAddedToolbarCoreElementState<T, S> colspan(Integer colspan);

	@Override
	IAddedToolbarCoreElementState<T, S> full();

}
