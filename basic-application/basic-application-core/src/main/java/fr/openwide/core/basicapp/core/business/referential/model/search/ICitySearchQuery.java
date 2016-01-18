package fr.openwide.core.basicapp.core.business.referential.model.search;

import org.hibernate.search.exception.SearchException;

import fr.openwide.core.basicapp.core.business.referential.model.City;


public interface ICitySearchQuery extends IAbstractGenericListItemSearchQuery<City, CitySort> {
	
	ICitySearchQuery label(String label) throws SearchException;
	
}
