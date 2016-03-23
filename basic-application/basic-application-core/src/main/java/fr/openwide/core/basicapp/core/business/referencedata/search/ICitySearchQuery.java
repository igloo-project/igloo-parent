package fr.openwide.core.basicapp.core.business.referencedata.search;

import org.hibernate.search.exception.SearchException;

import fr.openwide.core.basicapp.core.business.referencedata.model.City;
import fr.openwide.core.jpa.more.business.generic.model.search.GenericListItemSort;
import fr.openwide.core.jpa.more.business.generic.query.IGenericListItemSearchQuery;


public interface ICitySearchQuery extends IGenericListItemSearchQuery<City, GenericListItemSort, ICitySearchQuery> {
	ICitySearchQuery postalCode(String postalCode) throws SearchException;
}
