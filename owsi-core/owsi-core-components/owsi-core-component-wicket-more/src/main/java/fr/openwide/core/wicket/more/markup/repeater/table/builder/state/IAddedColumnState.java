package fr.openwide.core.wicket.more.markup.repeater.table.builder.state;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;

public interface IAddedColumnState<T, S extends ISort<?>> extends IColumnState<T, S> {
	
	IAddedColumnState<T, S> when(Condition condition);

}
