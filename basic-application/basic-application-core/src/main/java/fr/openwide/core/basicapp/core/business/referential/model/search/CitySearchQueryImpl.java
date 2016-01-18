package fr.openwide.core.basicapp.core.business.referential.model.search;

import org.hibernate.search.exception.SearchException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.basicapp.core.business.referential.model.City;

@Component
@Scope("prototype")
public class CitySearchQueryImpl extends AbstractGenericListItemSearchQueryImpl<City, CitySort> implements ICitySearchQuery {

	protected CitySearchQueryImpl() {
		super(City.class);
	}

	@Override
	public ICitySearchQuery label(String label) throws SearchException {
		must(matchAutocompleteIfGiven(
				label,
				ImmutableList.of(
						City.LABEL_AUTOCOMPLETE
				)
		));
		return this;
	}
}