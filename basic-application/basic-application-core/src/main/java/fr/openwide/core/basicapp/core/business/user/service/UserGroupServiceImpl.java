package fr.openwide.core.basicapp.core.business.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.user.dao.IUserGroupDao;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.jpa.security.business.person.service.AbstractPersonGroupServiceImpl;

@Service("personGroupService")
public class UserGroupServiceImpl extends AbstractPersonGroupServiceImpl<UserGroup, User>
		implements IUserGroupService {

	@Autowired
	private IHibernateSearchService hibernateSearchService;

	@Autowired
	private IUserService userService;

	@Autowired
	public UserGroupServiceImpl(IUserGroupDao userGroupDao) {
		super(userGroupDao);
	}

	@Override
	public List<UserGroup> searchAutocomplete(String searchPattern) throws ServiceException, SecurityServiceException {
		String[] searchFields = new String[] { Bindings.userGroup().name().getPath() };
		
		return hibernateSearchService.searchAutocomplete(getObjectClass(), searchFields, searchPattern);
	}
}
