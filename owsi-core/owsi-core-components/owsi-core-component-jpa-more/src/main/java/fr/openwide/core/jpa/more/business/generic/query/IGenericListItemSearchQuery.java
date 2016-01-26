package fr.openwide.core.jpa.more.business.generic.query;

import org.hibernate.search.exception.SearchException;

import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;
import fr.openwide.core.jpa.more.business.sort.ISort;

public interface IGenericListItemSearchQuery<T extends GenericListItem<T>, S extends ISort<?>> extends ISearchQuery<T, S> {
	
	IGenericListItemSearchQuery<T,S> label(String label) throws SearchException;
	
	IGenericListItemSearchQuery<T, S> enabled(EnabledFilter enabledFilter);
}
