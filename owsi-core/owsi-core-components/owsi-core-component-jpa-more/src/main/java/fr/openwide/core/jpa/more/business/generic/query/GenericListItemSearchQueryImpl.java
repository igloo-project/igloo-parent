package fr.openwide.core.jpa.more.business.generic.query;
import org.apache.lucene.search.SortField;
import org.hibernate.search.exception.SearchException;

import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItemBinding;
import fr.openwide.core.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;
import fr.openwide.core.jpa.more.business.sort.ISort;

public abstract class GenericListItemSearchQueryImpl<T extends GenericListItem<T>, S extends ISort<SortField>> extends AbstractHibernateSearchSearchQuery<T, S> implements IGenericListItemSearchQuery<T, S> {

	protected GenericListItemSearchQueryImpl(Class<T> clazz) {
		super(clazz);
	}

	@Override
	public IGenericListItemSearchQuery<T,S> enabled(EnabledFilter enabledFilter) {
		if (enabledFilter != null && !EnabledFilter.ALL.equals(enabledFilter)) {
			switch (enabledFilter) {
			case ENABLED_ONLY:
				must(matchIfGiven(new GenericListItemBinding<>().enabled(), Boolean.TRUE));
				break;
			case DISABLED_ONLY:
				must(matchIfGiven(new GenericListItemBinding<>().enabled(), Boolean.FALSE));
				break;
			default:
				throw new IllegalStateException(String.format("Unknown value for EnabledFilter : %s", enabledFilter));
			}
		} 
		return this;
	}

	@Override
	public abstract IGenericListItemSearchQuery<T,S> label(String label) throws SearchException;
}