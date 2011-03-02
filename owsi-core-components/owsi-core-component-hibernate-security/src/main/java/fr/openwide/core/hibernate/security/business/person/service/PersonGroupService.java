package fr.openwide.core.hibernate.security.business.person.service;

import java.util.List;

import fr.openwide.core.hibernate.business.generic.service.GenericEntityService;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.core.hibernate.security.business.person.model.CorePerson;
import fr.openwide.core.hibernate.security.business.person.model.CorePersonGroup;

public interface PersonGroupService extends GenericEntityService<Integer, CorePersonGroup> {

	CorePersonGroup getByName(String name);

	void addPerson(CorePersonGroup personGroup, CorePerson person) throws ServiceException, SecurityServiceException;
	
	void removePerson(CorePersonGroup personGroup, CorePerson person) throws ServiceException, SecurityServiceException;

	List<CorePerson> getPersonsFromPersonGroup(CorePersonGroup personGroup) throws ServiceException, SecurityServiceException;
	
}
