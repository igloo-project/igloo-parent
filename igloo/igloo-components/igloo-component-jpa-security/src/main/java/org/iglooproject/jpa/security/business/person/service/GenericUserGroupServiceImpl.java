package org.iglooproject.jpa.security.business.person.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.person.dao.IGenericUserGroupDao;
import org.iglooproject.jpa.security.business.person.model.GenericUser;
import org.iglooproject.jpa.security.business.person.model.GenericUserGroup;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericUserGroupServiceImpl<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends GenericEntityServiceImpl<Long, G>
		implements IGenericUserGroupService<G, U> {

	protected IGenericUserGroupDao<G, U> dao;

	@Autowired
	private IGenericUserService<U> userService;

	@Autowired
	public GenericUserGroupServiceImpl(IGenericUserGroupDao<G, U> dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public G getByName(String name) {
		return dao.getByName(name);
	}

	@Override
	public void addUser(G group, U user) throws ServiceException, SecurityServiceException {
		user.addGroup(group);
		update(group);
		userService.update(user);
	}

	@Override
	public void removeUser(G group, U user) throws ServiceException, SecurityServiceException {
		user.removeGroup(group);
		update(group);
		userService.update(user);
	}

}
