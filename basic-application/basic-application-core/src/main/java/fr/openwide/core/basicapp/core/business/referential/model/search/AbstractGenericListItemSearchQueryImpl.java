package fr.openwide.core.basicapp.core.business.referential.model.search;
import org.apache.lucene.search.SortField;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;
import fr.openwide.core.jpa.more.business.sort.ISort;

@Component
@Scope("prototype")
public class AbstractGenericListItemSearchQueryImpl<T extends GenericListItem<? super T>, S extends ISort<SortField>> extends AbstractHibernateSearchSearchQuery<T, S> implements IAbstractGenericListItemSearchQuery<T, S> {

	protected AbstractGenericListItemSearchQueryImpl(Class<T> clazz) {
		super(clazz);
	}

	@Override
	public IAbstractGenericListItemSearchQuery<T,S> enabled(EnabledFilter enabledFilter) {
		if (enabledFilter != null && !EnabledFilter.ALL.equals(enabledFilter)) {
			switch (enabledFilter) {
			case ENABLED_ONLY:
				must(matchIfGiven(Bindings.genericListItem().enabled(), Boolean.TRUE));
				break;
			case DISABLED_ONLY:
				must(matchIfGiven(Bindings.genericListItem().enabled(), Boolean.FALSE));
				break;
			default:
				throw new IllegalStateException(String.format("Unknown value for EnabledFilter : %s", enabledFilter));
			}
		} 
		return this;
	}
}