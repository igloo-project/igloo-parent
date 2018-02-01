package org.iglooproject.jpa.security.business.person.service;

import java.util.List;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.jpa.security.business.person.dao.IGenericUserDao;
import org.iglooproject.jpa.security.business.person.model.GenericSimpleUser;
import org.iglooproject.jpa.security.business.person.model.ISimpleUserBinding;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericSimpleUserServiceImpl<U extends GenericSimpleUser<U, ?>> extends GenericUserServiceImpl<U> {
	
	private static final ISimpleUserBinding BINDING = new ISimpleUserBinding();
	
	private static final String[] SEARCH_FIELDS = new String[] { BINDING.username().getPath(), BINDING.firstName().getPath(), BINDING.lastName().getPath() };
	
	private static final String[] AUTOCOMPLETE_SEARCH_FIELDS = new String[] { BINDING.username().getPath(), BINDING.firstName().getPath(), BINDING.lastName().getPath() };
	
	private static final Sort AUTOCOMPLETE_SORT = new Sort(new SortField(GenericSimpleUser.LAST_NAME_SORT_FIELD_NAME, SortField.Type.STRING),
			new SortField(GenericSimpleUser.FIRST_NAME_SORT_FIELD_NAME, SortField.Type.STRING));
	
	@Autowired
	private IHibernateSearchService hibernateSearchService;

	public GenericSimpleUserServiceImpl(IGenericUserDao<U> personDao) {
		super(personDao);
	}
	
	/**
	 * @deprecated use the ISearchQuery pattern instead.
	 */
	@Deprecated
	@Override
	public List<U> search(String searchPattern) throws ServiceException, SecurityServiceException {
		return hibernateSearchService.search(getObjectClass(), SEARCH_FIELDS, searchPattern);
	}
	
	/**
	 * @deprecated use the ISearchQuery pattern instead.
	 */
	@Deprecated
	@Override
	public <U2 extends U> List<U2> searchAutocomplete(Class<U2> clazz, String searchPattern, Integer limit, Integer offset) throws ServiceException, SecurityServiceException {
		return hibernateSearchService.searchAutocomplete(clazz, AUTOCOMPLETE_SEARCH_FIELDS, searchPattern,
				limit, offset, AUTOCOMPLETE_SORT);
	}

}
