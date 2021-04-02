package org.iglooproject.jpa.security.business.user.service;

import java.util.Date;
import java.util.Locale;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.business.user.dao.IGenericUserDao;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class GenericUserServiceImpl<U extends GenericUser<U, ?>>
		extends GenericEntityServiceImpl<Long, U>
		implements IGenericUserService<U>, ISecurityUserService<U> {

	private IGenericUserDao<U> dao;

	@Autowired
	private IAuthorityService authorityService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public GenericUserServiceImpl(IGenericUserDao<U> dao) {
		super(dao);
		this.dao = dao;
	}

	@Override
	protected void createEntity(U user) throws ServiceException, SecurityServiceException {
		super.createEntity(user);
		
		Date date = new Date();
		user.setCreationDate(date);
		user.setLastUpdateDate(date);
		
		if (user.getAuthorities().isEmpty()) {
			Authority defaultAuthority = authorityService.getByName(CoreAuthorityConstants.ROLE_AUTHENTICATED);
			if (defaultAuthority != null) {
				user.addAuthority(defaultAuthority);
				
				super.save(user);
			} else {
				throw new ServiceException("Default authority ROLE_AUTHENTICATED has not been created yet");
			}
		}
	}

	@Override
	protected void updateEntity(U user) throws ServiceException, SecurityServiceException {
		user.setLastUpdateDate(new Date());
		super.updateEntity(user);
	}

	@Override
	public U getByUsername(String username) {
		if (!StringUtils.hasText(username)) {
			return null;
		}
		return getByNaturalId(username);
	}

	@Override
	public U getByUsernameCaseInsensitive(String username) {
		if (!StringUtils.hasText(username)) {
			return null;
		}
		return dao.getByUsernameCaseInsensitive(username);
	}

	@Override
	public void setEnabled(U user, boolean enabled) throws ServiceException, SecurityServiceException {
		user.setEnabled(enabled);
		super.update(user);
	}

	@Override
	public void setPasswords(U user, String clearTextPassword) throws ServiceException, SecurityServiceException {
		user.setPasswordHash(passwordEncoder.encode(clearTextPassword));
		super.update(user);
	}

	@Override
	public void updateLastLoginDate(U user) throws ServiceException, SecurityServiceException {
		user.setLastLoginDate(new Date());
		super.updateEntity(user);
	}

	@Override
	public void updateLocale(U user, Locale locale) throws ServiceException, SecurityServiceException {
		user.setLocale(locale);
		super.updateEntity(user);
	}

	@Override
	public void addAuthority(U user, Authority authority) throws ServiceException, SecurityServiceException {
		user.addAuthority(authority);
		super.update(user);
	}

	@Override
	public void addAuthority(U user, String authorityName) throws ServiceException, SecurityServiceException {
		addAuthority(user, authorityService.getByName(authorityName));
	}

}
