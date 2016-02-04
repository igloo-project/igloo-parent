package fr.openwide.core.basicapp.core.business.referencedata.search;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.basicapp.core.business.referencedata.model.City;
import fr.openwide.core.jpa.more.business.generic.model.search.GenericListItemSort;
import fr.openwide.core.jpa.more.business.generic.query.AbstractGenericListItemHibernateSearchSearchQuery;

@Component
@Scope("prototype")
public class CitySearchQueryImpl extends AbstractGenericListItemHibernateSearchSearchQuery<City, GenericListItemSort, ICitySearchQuery>
		implements ICitySearchQuery {

	protected CitySearchQueryImpl() {
		super(City.class);
	}

	@Override
	public ICitySearchQuery label(String label) {
		must(matchAutocompleteIfGiven(
				label,
				ImmutableList.of(
						City.LABEL_AUTOCOMPLETE
				)
		));
		return this;
	}
}