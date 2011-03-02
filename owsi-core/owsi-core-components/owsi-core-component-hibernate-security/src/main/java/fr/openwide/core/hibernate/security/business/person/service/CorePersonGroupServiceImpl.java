package fr.openwide.core.hibernate.security.business.person.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.hibernate.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.core.hibernate.security.business.person.dao.PersonGroupDao;
import fr.openwide.core.hibernate.security.business.person.model.CorePerson;
import fr.openwide.core.hibernate.security.business.person.model.CorePersonGroup;

public class CorePersonGroupServiceImpl extends GenericEntityServiceImpl<Integer, CorePersonGroup> implements PersonGroupService {

	protected PersonGroupDao personGroupDao;
	
	@Autowired
	public CorePersonGroupServiceImpl(PersonGroupDao personGroupDao) {
		super(personGroupDao);
		
		this.personGroupDao = personGroupDao;
	}
	
	@Override
	public CorePersonGroup getByName(String name) {
		return getByField("name", name);
	}

	@Override
	public void addPerson(CorePersonGroup personGroup, CorePerson person) throws ServiceException, SecurityServiceException {
		personGroup.addPerson(person);
		update(personGroup);
	}
	
	@Override
	public void removePerson(CorePersonGroup personGroup, CorePerson person) throws ServiceException, SecurityServiceException {
		personGroup.removePerson(person);
		update(personGroup);
	}
	
	@Override
	public List<CorePerson> getPersonsFromPersonGroup(CorePersonGroup personGroup) throws ServiceException, SecurityServiceException {
		return personGroup.getPersons();
	}

}