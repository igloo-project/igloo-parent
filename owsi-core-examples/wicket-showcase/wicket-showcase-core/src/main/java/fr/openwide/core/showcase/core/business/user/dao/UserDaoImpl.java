package fr.openwide.core.showcase.core.business.user.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.jpa.security.business.person.dao.AbstractPersonDaoImpl;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.model.UserBinding;

@Repository("userDao")
public class UserDaoImpl extends AbstractPersonDaoImpl<User> implements IUserDao {
	
	private static final UserBinding USER = new UserBinding();
	
	private static final String[] AUTOCOMPLETE_FIELDS = new String[] {
		USER.firstName().getPath(),
		USER.lastName().getPath()
	};
	
	@Autowired
	private IHibernateSearchService hibernateSearchService;
	
	@Override
	public List<User> searchAutocomplete(String searchPattern, Integer limit) throws ServiceException {
		return hibernateSearchService.searchAutocomplete(User.class, AUTOCOMPLETE_FIELDS, searchPattern, limit, null, null);
	}

}
