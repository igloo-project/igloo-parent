package fr.openwide.core.jpa.more.business.generic.query;

import java.util.Map;

import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.ISort.SortOrder;

public interface IGenericListItemSearchQuery
		<
		T extends GenericListItem<? super T>,
		S extends ISort<?>,
		Q extends IGenericListItemSearchQuery<T, S, Q>
		>
		extends ISearchQuery<T, S> {
	
	Q label(String label);
	
	Q code(String code);
	
	Q enabled(EnabledFilter enabledFilter);
	
	@Override
	Q sort(Map<S, SortOrder> sortMap);
	
}
