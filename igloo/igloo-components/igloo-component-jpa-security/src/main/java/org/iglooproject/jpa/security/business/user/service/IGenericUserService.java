package org.iglooproject.jpa.security.business.user.service;

import java.util.Locale;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.security.business.user.model.GenericUserGroup;

public interface IGenericUserService<U extends GenericUser<U, G>, G extends GenericUserGroup<G, U>> extends IGenericEntityService<Long, U>, ISecurityUserService<U> {

	void setEnabled(U user, boolean enabled) throws ServiceException, SecurityServiceException;

	void setPasswords(U user, String clearTextPassword) throws ServiceException, SecurityServiceException;

	void updateLastLoginDate(U user) throws ServiceException, SecurityServiceException;

	void updateLocale(U user, Locale locale) throws ServiceException, SecurityServiceException;

	void addGroup(U user, G group) throws ServiceException, SecurityServiceException;

	void removeGroup(U user, G group) throws ServiceException, SecurityServiceException;

	void addAuthority(U user, Authority authority) throws ServiceException, SecurityServiceException;

	void addAuthority(U user, String authorityName) throws ServiceException, SecurityServiceException;

}
