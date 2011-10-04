package fr.openwide.core.jpa.security.business.person.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.dao.IPersonGroupDao;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.model.AbstractPersonGroup;
import fr.openwide.core.jpa.security.business.person.model.AbstractPersonGroup_;

public abstract class AbstractPersonGroupServiceImpl<G extends AbstractPersonGroup<G, P>, P extends AbstractPerson<P>>
		extends GenericEntityServiceImpl<Integer, G> implements IPersonGroupService<G, P> {

	protected IPersonGroupDao<G, P> personGroupDao;
	
	@Autowired
	public AbstractPersonGroupServiceImpl(IPersonGroupDao<G, P> personGroupDao) {
		super(personGroupDao);
		
		this.personGroupDao = personGroupDao;
	}
	
	@Override
	public G getByName(String name) {
		return getByField(AbstractPersonGroup_.name, name);
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