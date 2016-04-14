package fr.openwide.core.jpa.more.business.generic.query;

import org.apache.lucene.search.SortField;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.sort.ISort;

public class SimpleGenericListItemHibernateSearchSearchQueryImpl
		<
		T extends GenericListItem<? super T>,
		S extends ISort<SortField>
		>
		extends AbstractGenericListItemHibernateSearchSearchQueryImpl<T, S, ISimpleGenericListItemSearchQuery<T, S>>
		implements ISimpleGenericListItemSearchQuery<T, S> {

	public SimpleGenericListItemHibernateSearchSearchQueryImpl(Class<T> clazz) {
		super(clazz);
	}

}
