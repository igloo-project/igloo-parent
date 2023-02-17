package org.iglooproject.jpa.security.business.user.service;

import java.util.List;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.dao.IGenericUserGroupDao;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.security.business.user.model.GenericUserGroup;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericUserGroupServiceImpl<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends GenericEntityServiceImpl<Long, G>
		implements IGenericUserGroupService<G, U> {

	protected IGenericUserGroupDao<G, U> dao;

	@Autowired
	public GenericUserGroupServiceImpl(IGenericUserGroupDao<G, U> dao) {
		super(dao);
		this.dao = dao;
	}

	@Override
	public void delete(G entity) throws ServiceException, SecurityServiceException {
		List.copyOf(entity.getUsers()).forEach(u -> u.removeGroup(entity));
		super.delete(entity);
	}

	@Override
	public G getByName(String name) {
		return dao.getByName(name);
	}

}
