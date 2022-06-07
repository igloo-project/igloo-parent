package org.iglooproject.wicket.more.markup.repeater.table.builder.state;

import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.condition.Condition;
import org.iglooproject.wicket.more.markup.html.sort.ISortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;

public interface IAddedCoreColumnState<T, S extends ISort<?>> extends IAddedColumnState<T, S> {

	@Override
	IAddedCoreColumnState<T, S> when(Condition condition);

	IAddedCoreColumnState<T, S> withClass(String cssClass);
	
	IAddedCoreColumnState<T, S> withSort(S sort);
	
	IAddedCoreColumnState<T, S> withSort(S sort, ISortIconStyle sortIconStyle);

	IAddedCoreColumnState<T, S> withSort(S sort, ISortIconStyle sortIconStyle, CycleMode cycleMode);

}
