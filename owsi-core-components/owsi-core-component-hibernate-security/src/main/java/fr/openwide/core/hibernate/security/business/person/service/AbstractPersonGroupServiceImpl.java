package fr.openwide.core.hibernate.security.business.person.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.hibernate.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.core.hibernate.security.business.person.dao.PersonGroupDao;
import fr.openwide.core.hibernate.security.business.person.model.AbstractPerson;
import fr.openwide.core.hibernate.security.business.person.model.AbstractPersonGroup;

public abstract class AbstractPersonGroupServiceImpl<G extends AbstractPersonGroup<G, P>, P extends AbstractPerson<P>>
		extends GenericEntityServiceImpl<Integer, G> implements PersonGroupService<G, P> {

	protected PersonGroupDao<G, P> personGroupDao;
	
	@Autowired
	public AbstractPersonGroupServiceImpl(PersonGroupDao<G, P> personGroupDao) {
		super(personGroupDao);
		
		this.personGroupDao = personGroupDao;
	}
	
	@Override
	public G getByName(String name) {
		return getByField("name", name);
	}

	@Override
	public void addPerson(G personGroup, P person) throws ServiceException, SecurityServiceException {
		personGroup.addPerson(person);
		update(personGroup);
	}
	
	@Override
	public void removePerson(G personGroup, P person) throws ServiceException, SecurityServiceException {
		personGroup.removePerson(person);
		update(personGroup);
	}
	
	@Override
	public List<P> getPersonsFromPersonGroup(G personGroup) throws ServiceException, SecurityServiceException {
		return personGroup.getPersons();
	}

}