package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.toolbar.state;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;

public interface IAddedToolbarElementState<T, S extends ISort<?>> extends IToolbarElementState<T, S> {

	IAddedToolbarElementState<T, S> colspan(long colspan);

	IAddedToolbarElementState<T, S> full();

	IAddedToolbarElementState<T, S> when(Condition condition);

}
