package fr.openwide.core.jpa.security.business.person.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.authority.service.IAuthorityService;
import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.jpa.security.business.person.dao.IPersonDao;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.model.IPersonBinding;

public abstract class AbstractPersonServiceImpl<P extends AbstractPerson<P>>
		extends GenericEntityServiceImpl<Long, P>
		implements IPersonService<P> {
	
	private static final IPersonBinding BINDING = new IPersonBinding();
	
	public static final String[] AUTOCOMPLETE_SEARCH_FIELDS = new String[] { BINDING.userName().getPath(), BINDING.firstName().getPath(), BINDING.lastName().getPath() };
	
	public static final Sort AUTOCOMPLETE_SORT = new Sort(new SortField(AbstractPerson.LAST_NAME_SORT_FIELD_NAME, SortField.STRING),
			new SortField(AbstractPerson.FIRST_NAME_SORT_FIELD_NAME, SortField.STRING));

	@Autowired
	private IAuthorityService authorityService;
	
	@Autowired
	private IHibernateSearchService hibernateSearchService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private IPersonDao<P> personDao;
	
	@Autowired
	public AbstractPersonServiceImpl(IPersonDao<P> personDao) {
		super(personDao);
		this.personDao = personDao;
	}
	
	@Override
	public P getByUserName(String userName) {
		return getByNaturalId(userName);
	}
	
	@Override
	public P getByUserNameCaseInsensitive(String userName) {
		return personDao.getByUserNameCaseInsensitive(userName);
	}
	
	@Override
	public List<P> search(String searchPattern) throws ServiceException, SecurityServiceException {
		String[] searchFields = new String[] { BINDING.userName().getPath(), BINDING.firstName().getPath(), BINDING.lastName().getPath() };
		
		return hibernateSearchService.search(getObjectClass(), searchFields, searchPattern);
	}
	
	@Override
	public List<P> searchAutocomplete(String searchPattern) throws ServiceException, SecurityServiceException {
		return searchAutocomplete(searchPattern, null, null);
	}
	
	@Override
	public List<P> searchAutocomplete(String searchPattern, Integer limit, Integer offset) throws ServiceException, SecurityServiceException {
		return hibernateSearchService.searchAutocomplete(getObjectClass(), AUTOCOMPLETE_SEARCH_FIELDS, searchPattern,
				limit, offset, AUTOCOMPLETE_SORT);
	}
	
	@Override
	protected void createEntity(P person) throws ServiceException, SecurityServiceException {
		super.createEntity(person);
		
		Date date = new Date();
		person.setCreationDate(date);
		person.setLastUpdateDate(date);
		
		if (person.getAuthorities().size() == 0) {
			Authority defaultAuthority = authorityService.getByName(CoreAuthorityConstants.ROLE_AUTHENTICATED);
			if (defaultAuthority != null) {
				person.addAuthority(defaultAuthority);
				
				super.save(person);
			} else {
				throw new ServiceException("Default authority ROLE_AUTHENTICATED has not been created yet");
			}
		}
	}
	
	@Override
	protected void updateEntity(P person) throws ServiceException, SecurityServiceException {
		person.setLastUpdateDate(new Date());
		super.updateEntity(person);
	}
	
	@Override
	public void updateProfileInformation(P person) throws ServiceException, SecurityServiceException {
		super.update(person);
	}
	
	@Override
	public void addAuthority(P person, Authority authority) throws ServiceException, SecurityServiceException {
		person.addAuthority(authority);
		super.update(person);
	}
	
	@Override
	public void addAuthority(P person, String authorityName) throws ServiceException, SecurityServiceException {
		addAuthority(person, authorityService.getByName(authorityName));
	}
	
	@Override
	public void setActive(P person, boolean active) throws ServiceException, SecurityServiceException {
		person.setActive(active);
		super.update(person);
	}
	
	@Override
	public Long countActive() {
		return personDao.countActive();
	}
	
	@Override
	public void setPasswords(P person, String clearTextPassword) throws ServiceException, SecurityServiceException {
		person.setPasswordHash(passwordEncoder.encode(clearTextPassword));
		super.update(person);
	}

	@Override
	public void updateLastLoginDate(P person) throws ServiceException, SecurityServiceException {
		person.setLastLoginDate(new Date());
		super.updateEntity(person);
	}
	
	@Override
	public void updateLocale(P person, Locale locale) throws ServiceException, SecurityServiceException {
		person.setLocale(locale);
		super.updateEntity(person);
	}

}