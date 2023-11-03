package org.iglooproject.jpa.more.search.query;

import java.util.Map;
import java.util.function.Function;

import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.iglooproject.jpa.more.business.sort.ISort;

public interface IHibernateSearchSearchQuery<T, S extends ISort<Function<SearchSortFactory, SortFinalStep>>, D extends ISearchQueryData<T>> extends ISearchQuery<T, S, D> {

	default Function<SearchSortFactory, SortFinalStep> sortContributor(Map<S, ISort.SortOrder> sorts) {
		if (sorts == null) {
			return f -> f.composite();
		}
		
		return f -> f.composite(c ->
			sorts.entrySet()
				.forEach(e ->
					e.getKey().getSortFields(e.getValue())
						.forEach(s -> c.add(s.apply(f)))
				)
		);
	}

}
