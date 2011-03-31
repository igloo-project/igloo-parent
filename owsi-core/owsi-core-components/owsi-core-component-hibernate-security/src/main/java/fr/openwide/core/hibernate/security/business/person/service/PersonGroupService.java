package fr.openwide.core.hibernate.security.business.person.service;

import java.util.List;

import fr.openwide.core.hibernate.business.generic.service.GenericEntityService;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.core.hibernate.security.business.person.model.AbstractPerson;
import fr.openwide.core.hibernate.security.business.person.model.AbstractPersonGroup;

public interface PersonGroupService<G extends AbstractPersonGroup<G, P>, P extends AbstractPerson<P>>
		extends GenericEntityService<Integer, G> {

	G getByName(String name);

	void addPerson(G personGroup, P person) throws ServiceException, SecurityServiceException;
	
	void removePerson(G personGroup, P person) throws ServiceException, SecurityServiceException;

	List<P> getPersonsFromPersonGroup(G personGroup) throws ServiceException, SecurityServiceException;
	
}
