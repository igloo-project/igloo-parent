package org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar.state;

import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.condition.Condition;

public interface IAddedToolbarLabelElementState<T, S extends ISort<?>> extends IAddedToolbarElementState<T, S> {

	@Override
	IAddedToolbarLabelElementState<T, S> when(Condition condition);

	@Override
	IAddedToolbarLabelElementState<T, S> withClass(String cssClass);

	@Override
	IAddedToolbarLabelElementState<T, S> colspan(Integer colspan);

	@Override
	IAddedToolbarLabelElementState<T, S> full();

}
