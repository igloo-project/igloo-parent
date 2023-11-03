package org.iglooproject.basicapp.core.business.referencedata.search;

import java.util.function.BiConsumer;

import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import org.iglooproject.basicapp.core.business.referencedata.model.City;

public class CitySearchQueryImpl extends AbstractReferenceDataSearchQueryImpl<City, CitySort, CitySearchQueryData> implements ICitySearchQuery {

	public CitySearchQueryImpl() {
		super(City.class);
	}

	@Override
	protected BiConsumer<SearchPredicateFactory, SimpleBooleanPredicateClausesCollector<?>> predicateContributor(CitySearchQueryData data) {
		return super.predicateContributor(data)
			.andThen((f, root) -> {
				if (data.getPostalCode() != null) {
					root.add(f.match().field(City.POSTAL_CODE).matching(data.getPostalCode()));
				}
			});
	}

}
