package fr.openwide.core.jpa.security.business.person.service;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.authority.service.IAuthorityService;
import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.jpa.security.business.person.dao.IGenericUserDao;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;

public abstract class GenericUserServiceImpl<U extends GenericUser<U, ?>>
		extends GenericEntityServiceImpl<Long, U>
		implements IGenericUserService<U> {
	
	@Autowired
	private IAuthorityService authorityService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private IGenericUserDao<U> personDao;
	
	@Autowired
	public GenericUserServiceImpl(IGenericUserDao<U> personDao) {
		super(personDao);
		this.personDao = personDao;
	}
	
	@Override
	public U getByUserName(String userName) {
		return getByNaturalId(userName);
	}
	
	@Override
	public U getByUserNameCaseInsensitive(String userName) {
		return personDao.getByUserNameCaseInsensitive(userName);
	}
	
	@Override
	protected void createEntity(U person) throws ServiceException, SecurityServiceException {
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
	protected void updateEntity(U person) throws ServiceException, SecurityServiceException {
		person.setLastUpdateDate(new Date());
		super.updateEntity(person);
	}
	
	@Override
	public void updateProfileInformation(U person) throws ServiceException, SecurityServiceException {
		super.update(person);
	}
	
	@Override
	public void addAuthority(U person, Authority authority) throws ServiceException, SecurityServiceException {
		person.addAuthority(authority);
		super.update(person);
	}
	
	@Override
	public void addAuthority(U person, String authorityName) throws ServiceException, SecurityServiceException {
		addAuthority(person, authorityService.getByName(authorityName));
	}
	
	@Override
	public void setActive(U person, boolean active) throws ServiceException, SecurityServiceException {
		person.setActive(active);
		super.update(person);
	}
	
	@Override
	public Long countActive() {
		return personDao.countActive();
	}
	
	@Override
	public void setPasswords(U person, String clearTextPassword) throws ServiceException, SecurityServiceException {
		person.setPasswordHash(passwordEncoder.encode(clearTextPassword));
		super.update(person);
	}

	@Override
	public void updateLastLoginDate(U person) throws ServiceException, SecurityServiceException {
		person.setLastLoginDate(new Date());
		super.updateEntity(person);
	}
	
	@Override
	public void updateLocale(U person, Locale locale) throws ServiceException, SecurityServiceException {
		person.setLocale(locale);
		super.updateEntity(person);
	}

}