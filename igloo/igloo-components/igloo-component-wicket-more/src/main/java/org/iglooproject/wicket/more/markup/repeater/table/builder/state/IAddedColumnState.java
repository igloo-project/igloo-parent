package org.iglooproject.wicket.more.markup.repeater.table.builder.state;

import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.api.condition.Condition;

public interface IAddedColumnState<T, S extends ISort<?>> extends IColumnState<T, S> {
	
	IAddedColumnState<T, S> when(Condition condition);

}
