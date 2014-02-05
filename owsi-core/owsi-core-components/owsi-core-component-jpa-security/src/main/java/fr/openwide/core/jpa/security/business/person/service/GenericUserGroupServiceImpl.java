package fr.openwide.core.jpa.security.business.person.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.jpa.security.business.person.dao.IGenericUserGroupDao;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;
import fr.openwide.core.jpa.security.business.person.model.GenericUserGroup;
import fr.openwide.core.jpa.security.business.person.model.GenericUserGroup_;
import fr.openwide.core.jpa.security.business.person.model.IUserGroupBinding;

public abstract class GenericUserGroupServiceImpl<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends GenericEntityServiceImpl<Long, G> implements IGenericUserGroupService<G, U> {
	
	private static final IUserGroupBinding BINDING = new IUserGroupBinding();
	
	private static final String[] SEARCH_FIELDS = new String[] { BINDING.name().getPath() };

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
		return getByField(GenericUserGroup_.name, name);
	}

	@Override
	public void addUser(G personGroup, U person) throws ServiceException, SecurityServiceException {
		personGroup.addPerson(person);
		update(personGroup);
	}
	
	@Override
	public void removeUser(G personGroup, U person) throws ServiceException, SecurityServiceException {
		personGroup.removePerson(person);
		update(personGroup);
	}
	
	@Override
	public Set<U> getUsersFromUserGroup(G personGroup) throws ServiceException, SecurityServiceException {
		return personGroup.getPersons();
	}

	@Override
	public List<G> searchAutocomplete(String searchPattern) throws ServiceException, SecurityServiceException {
		return hibernateSearchService.searchAutocomplete(getObjectClass(), SEARCH_FIELDS, searchPattern);
	}

}