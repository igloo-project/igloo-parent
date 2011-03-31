package fr.openwide.core.hibernate.security.business.person.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.hibernate.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.core.hibernate.search.service.HibernateSearchService;
import fr.openwide.core.hibernate.security.business.authority.model.Authority;
import fr.openwide.core.hibernate.security.business.authority.service.AuthorityService;
import fr.openwide.core.hibernate.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.hibernate.security.business.person.dao.PersonDao;
import fr.openwide.core.hibernate.security.business.person.model.AbstractPerson;
import fr.openwide.core.hibernate.security.business.person.model.PersonBinding;

public abstract class AbstractPersonServiceImpl<P extends AbstractPerson<P>>
		extends GenericEntityServiceImpl<Integer, P>
		implements PersonService<P> {
	
	private static final PersonBinding BINDING = new PersonBinding();

	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private HibernateSearchService hibernateSearchService;
	
	private PersonDao<P> personDao;
	
	@Autowired
	public AbstractPersonServiceImpl(PersonDao<P> personDao) {
		super(personDao);
		this.personDao = personDao;
	}
	
	@Override
	public P getByUserName(String userName) {
		return getByField("userName", userName);
	}
	
	@Override
	public List<P> search(String searchPattern) throws ServiceException, SecurityServiceException {
		String[] searchFields = new String[] { BINDING.userName().getPath(), BINDING.firstName().getPath(), BINDING.lastName().getPath() };
		
		return hibernateSearchService.search(getObjectClass(), searchFields, searchPattern);
	}
	
	@Override
	public List<P> searchAutocomplete(String searchPattern) throws ServiceException, SecurityServiceException {
		String[] searchFields = new String[] { BINDING.userName().getPath(), BINDING.firstName().getPath(), BINDING.lastName().getPath() };
		
		return hibernateSearchService.searchAutocomplete(getObjectClass(), searchFields, searchPattern);
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
		person.setMd5Password(DigestUtils.md5Hex(clearTextPassword));
		super.update(person);
	}
	
	@Override
	public boolean comparePasswordToMd5Passwords(P person, String clearTextPassword) {
		String md5Password = person.getMd5Password();
		if (md5Password != null) {
			return md5Password.equals(DigestUtils.md5Hex(clearTextPassword));
		} else {
			return false;
		}
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