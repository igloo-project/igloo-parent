package fr.openwide.core.jpa.more.business.generic.query;
import java.util.Map;

import org.apache.lucene.search.SortField;

import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItemBinding;
import fr.openwide.core.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.ISort.SortOrder;

public abstract class AbstractGenericListItemHibernateSearchSearchQueryImpl
		<
		T extends GenericListItem<? super T>,
		S extends ISort<SortField>,
		Q extends IGenericListItemSearchQuery<T, S, Q>
		>
		extends AbstractHibernateSearchSearchQuery<T, S> implements IGenericListItemSearchQuery<T, S, Q> {

	@SafeVarargs
	public AbstractGenericListItemHibernateSearchSearchQueryImpl(Class<? extends T> clazz, S... defaultSorts) {
		super(clazz, defaultSorts);
	}

	@SafeVarargs
	public AbstractGenericListItemHibernateSearchSearchQueryImpl(Class<? extends T>[] classes, S... defaultSorts) {
		super(classes, defaultSorts);
	}

	@SuppressWarnings("unchecked")
	protected final Q thisAsQ() {
		return (Q) this;
	}

	@Override
	public Q label(String label) {
		must(matchIfGiven(
				GenericListItem.LABEL_FIELD_NAME,
				label
		));
		return thisAsQ();
	}

	@Override
	public Q code(String code) {
		must(matchIfGiven(
				GenericListItem.CODE_FIELD_NAME,
				code
		));
		return thisAsQ();
	}

	@Override
	public Q enabled(EnabledFilter enabledFilter) {
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
		return thisAsQ();
	}
	
	@Override
	public Q sort(Map<S, SortOrder> sortMap) {
		super.sort(sortMap);
		return thisAsQ();
	}
}