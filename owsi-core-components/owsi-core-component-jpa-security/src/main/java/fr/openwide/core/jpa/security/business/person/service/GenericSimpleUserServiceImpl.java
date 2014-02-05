package fr.openwide.core.jpa.security.business.person.service;

import java.util.List;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.jpa.security.business.person.dao.IGenericUserDao;
import fr.openwide.core.jpa.security.business.person.model.GenericSimpleUser;
import fr.openwide.core.jpa.security.business.person.model.ISimpleUserBinding;

public abstract class GenericSimpleUserServiceImpl<U extends GenericSimpleUser<U, ?>> extends GenericUserServiceImpl<U> {
	
	private static final ISimpleUserBinding BINDING = new ISimpleUserBinding();
	
	private static final String[] SEARCH_FIELDS = new String[] { BINDING.userName().getPath(), BINDING.firstName().getPath(), BINDING.lastName().getPath() };
	
	private static final String[] AUTOCOMPLETE_SEARCH_FIELDS = new String[] { BINDING.userName().getPath(), BINDING.firstName().getPath(), BINDING.lastName().getPath() };
	
	private static final Sort AUTOCOMPLETE_SORT = new Sort(new SortField(GenericSimpleUser.LAST_NAME_SORT_FIELD_NAME, SortField.STRING),
			new SortField(GenericSimpleUser.FIRST_NAME_SORT_FIELD_NAME, SortField.STRING));
	
	@Autowired
	private IHibernateSearchService hibernateSearchService;

	public GenericSimpleUserServiceImpl(IGenericUserDao<U> personDao) {
		super(personDao);
	}
	
	@Override
	public List<U> search(String searchPattern) throws ServiceException, SecurityServiceException {
		return hibernateSearchService.search(getObjectClass(), SEARCH_FIELDS, searchPattern);
	}
	
	@Override
	public List<U> searchAutocomplete(String searchPattern, Integer limit, Integer offset) throws ServiceException, SecurityServiceException {
		return hibernateSearchService.searchAutocomplete(getObjectClass(), AUTOCOMPLETE_SEARCH_FIELDS, searchPattern,
				limit, offset, AUTOCOMPLETE_SORT);
	}

}
