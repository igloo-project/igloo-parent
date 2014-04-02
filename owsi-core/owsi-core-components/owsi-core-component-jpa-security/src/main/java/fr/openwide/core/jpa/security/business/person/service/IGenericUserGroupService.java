package fr.openwide.core.jpa.security.business.person.service;

import java.util.List;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;
import fr.openwide.core.jpa.security.business.person.model.GenericUserGroup;

public interface IGenericUserGroupService<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends IGenericEntityService<Long, G> {

	G getByName(String name);

	void addUser(G group, U user) throws ServiceException, SecurityServiceException;
	
	void removeUser(G group, U user) throws ServiceException, SecurityServiceException;

	List<G> searchAutocomplete(String searchPattern) throws ServiceException, SecurityServiceException;
	
}
