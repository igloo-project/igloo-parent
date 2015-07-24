package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.toolbar.state;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;

public interface IAddedToolbarLabelElementState<T, S extends ISort<?>> extends IAddedToolbarCoreElementState<T, S> {

	@Override
	IAddedToolbarLabelElementState<T, S> when(Condition condition);

	@Override
	IAddedToolbarLabelElementState<T, S> withClass(String cssClass);

}
