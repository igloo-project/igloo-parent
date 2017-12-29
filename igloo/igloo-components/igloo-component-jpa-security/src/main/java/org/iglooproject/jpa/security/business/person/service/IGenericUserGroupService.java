package org.iglooproject.jpa.security.business.person.service;

import java.util.List;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.person.model.GenericUser;
import org.iglooproject.jpa.security.business.person.model.GenericUserGroup;

public interface IGenericUserGroupService<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends IGenericEntityService<Long, G> {

	G getByName(String name);

	void addUser(G group, U user) throws ServiceException, SecurityServiceException;
	
	void removeUser(G group, U user) throws ServiceException, SecurityServiceException;

	/**
	 * @deprecated Implement your own search query instead, either through a custom DAO or
	 * through {@link org.iglooproject.jpa.more.business.search.query.ISearchQuery<T, S>} as defined in
	 * igloo-component-jpa-more. See in particular
	 * {@link org.iglooproject.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery<T, S>}.
	 */
	List<G> searchAutocomplete(String searchPattern) throws ServiceException, SecurityServiceException;
	
}
