package org.iglooproject.jpa.security.business.person.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.person.dao.IGenericUserGroupDao;
import org.iglooproject.jpa.security.business.person.model.GenericUser;
import org.iglooproject.jpa.security.business.person.model.GenericUserGroup;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericUserGroupServiceImpl<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends GenericEntityServiceImpl<Long, G> implements IGenericUserGroupService<G, U> {
	
	@Autowired
	private IGenericUserService<U> userService;
	
	protected IGenericUserGroupDao<G, U> personGroupDao;
	
	@Autowired
	public GenericUserGroupServiceImpl(IGenericUserGroupDao<G, U> personGroupDao) {
		super(personGroupDao);
		
		this.personGroupDao = personGroupDao;
	}
	
	@Override
	public G getByName(String name) {
		return personGroupDao.getByName(name);
	}

	@Override
	public void addUser(G group, U person) throws ServiceException, SecurityServiceException {
		person.addGroup(group);
		update(group);
		userService.update(person);
	}
	
	@Override
	public void removeUser(G group, U person) throws ServiceException, SecurityServiceException {
		person.removeGroup(group);
		update(group);
		userService.update(person);
	}

}