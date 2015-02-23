package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.state;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.sort.ISortIconStyle;
import fr.openwide.core.wicket.more.markup.html.sort.TableSortLink.CycleMode;

public interface IAddedCoreColumnState<T, S extends ISort<?>> extends IAddedColumnState<T, S> {

	@Override
	IAddedCoreColumnState<T, S> when(Condition condition);

	IAddedCoreColumnState<T, S> withClass(String cssClass);
	
	IAddedCoreColumnState<T, S> withSort(S sort);
	
	IAddedCoreColumnState<T, S> withSort(S sort, ISortIconStyle sortIconStyle);

	IAddedCoreColumnState<T, S> withSort(S sort, ISortIconStyle sortIconStyle, CycleMode cycleMode);

}
