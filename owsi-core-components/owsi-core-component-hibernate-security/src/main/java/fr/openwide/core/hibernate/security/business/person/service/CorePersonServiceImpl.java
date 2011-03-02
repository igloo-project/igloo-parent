package fr.openwide.core.hibernate.security.business.person.service;

import java.util.Date;
import java.util.List;

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
import fr.openwide.core.hibernate.security.business.person.model.CorePerson;
import fr.openwide.core.hibernate.security.business.person.model.AbstractPersonBinding;

public class CorePersonServiceImpl extends GenericEntityServiceImpl<Integer, CorePerson>
		implements PersonService {
	
	private static final AbstractPersonBinding BINDING = new AbstractPersonBinding();

	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private HibernateSearchService hibernateSearchService;
	
	private PersonDao personDao;
	
	@Autowired
	public CorePersonServiceImpl(PersonDao personDao) {
		super(personDao);
		this.personDao = personDao;
	}
	
	@Override
	public CorePerson getByUserName(String userName) {
		return getByField("userName", userName);
	}
	
	@Override
	public List<CorePerson> search(String searchPattern) throws ServiceException, SecurityServiceException {
		String[] searchFields = new String[] { BINDING.userName().getPath(), BINDING.firstName().getPath(), BINDING.lastName().getPath() };
		
		return hibernateSearchService.search(getObjectClass(), searchFields, searchPattern);
	}
	
	@Override
	public List<CorePerson> searchAutocomplete(String searchPattern) throws ServiceException, SecurityServiceException {
		String[] searchFields = new String[] { BINDING.userName().getPath(), BINDING.firstName().getPath(), BINDING.lastName().getPath() };
		
		return hibernateSearchService.searchAutocomplete(getObjectClass(), searchFields, searchPattern);
	}
	
	@Override
	protected void createEntity(CorePerson person) throws ServiceException, SecurityServiceException {
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
	protected void updateEntity(CorePerson person) throws ServiceException, SecurityServiceException {
		person.setLastUpdateDate(new Date());
		super.updateEntity(person);
	}
	
	@Override
	public void updateProfileInformation(CorePerson person) throws ServiceException, SecurityServiceException {
		person.setLastLoginDate(new Date());
		super.updateEntity(person);
	}
	
	@Override
	public void addAuthority(CorePerson person, Authority authority) throws ServiceException, SecurityServiceException {
		person.addAuthority(authority);
		super.update(person);
	}
	
	@Override
	public void addAuthority(CorePerson person, String authorityName) throws ServiceException, SecurityServiceException {
		addAuthority(person, authorityService.getByName(authorityName));
	}
	
	@Override
	public void setActive(CorePerson person, boolean active) throws ServiceException, SecurityServiceException {
		person.setActive(active);
		super.update(person);
	}
	
	@Override
	public Long countActive() {
		return personDao.countActive();
	}
	
	@Override
	public void setPasswords(CorePerson person, String clearTextPassword) throws ServiceException, SecurityServiceException {
		person.setMd5Password(DigestUtils.md5Hex(clearTextPassword));
		super.update(person);
	}
	
	@Override
	public boolean comparePasswordToMd5Passwords(CorePerson person, String clearTextPassword) {
		String md5Password = person.getMd5Password();
		if (md5Password != null) {
			return md5Password.equals(DigestUtils.md5Hex(clearTextPassword));
		} else {
			return false;
		}
	}

	@Override
	public void updateLastLoginDate(CorePerson person) throws ServiceException, SecurityServiceException {
		person.setLastLoginDate(new Date());
		super.update(person);
	}

}