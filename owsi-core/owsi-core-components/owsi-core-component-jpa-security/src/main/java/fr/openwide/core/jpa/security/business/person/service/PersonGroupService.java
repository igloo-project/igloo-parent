package fr.openwide.core.jpa.security.business.person.service;

import java.util.List;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.model.AbstractPersonGroup;

public interface PersonGroupService<G extends AbstractPersonGroup<G, P>, P extends AbstractPerson<P>>
		extends IGenericEntityService<Integer, G> {

	G getByName(String name);

	void addPerson(G personGroup, P person) throws ServiceException, SecurityServiceException;
	
	void removePerson(G personGroup, P person) throws ServiceException, SecurityServiceException;

	List<P> getPersonsFromPersonGroup(G personGroup) throws ServiceException, SecurityServiceException;
	
}
