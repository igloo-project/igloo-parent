package fr.openwide.core.basicapp.core.business.referential.model.search;

import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;
import fr.openwide.core.jpa.more.business.sort.ISort;

public interface IAbstractGenericListItemSearchQuery<T extends GenericListItem<? super T>, S extends ISort<?>> extends ISearchQuery<T, S> {
	
	IAbstractGenericListItemSearchQuery<T, S> enabled(EnabledFilter enabledFilter);
}
