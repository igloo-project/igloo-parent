package org.iglooproject.jpa.security.business.person.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.jpa.security.business.person.dao.IGenericUserGroupDao;
import org.iglooproject.jpa.security.business.person.model.GenericUser;
import org.iglooproject.jpa.security.business.person.model.GenericUserGroup;
import org.iglooproject.jpa.security.business.person.model.IUserGroupBinding;

public abstract class GenericUserGroupServiceImpl<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends GenericEntityServiceImpl<Long, G> implements IGenericUserGroupService<G, U> {
	
	private static final IUserGroupBinding BINDING = new IUserGroupBinding();
	
	private static final String[] SEARCH_FIELDS = new String[] { BINDING.name().getPath() };

	@Autowired
	private IGenericUserService<U> userService;
	
	@Autowired
	private IHibernateSearchService hibernateSearchService;

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

	@Override
	@Deprecated
	public List<G> searchAutocomplete(String searchPattern) throws ServiceException, SecurityServiceException {
		return hibernateSearchService.searchAutocomplete(getObjectClass(), SEARCH_FIELDS, searchPattern);
	}

}