package org.iglooproject.basicapp.core.business.referencedata.search;

import java.util.function.Function;

import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;

public interface IAbstractReferenceDataSearchQuery<T extends ReferenceData<? super T>, S extends ISort<Function<SearchSortFactory, SortFinalStep>>, D extends ISearchQueryData<T>> extends IHibernateSearchSearchQuery<T, S, D> {

}
