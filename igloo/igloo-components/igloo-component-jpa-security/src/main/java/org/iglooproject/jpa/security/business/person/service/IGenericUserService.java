package org.iglooproject.jpa.security.business.person.service;

import java.util.List;
import java.util.Locale;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.person.model.GenericUser;

public interface IGenericUserService<U extends GenericUser<U, ?>> extends IGenericEntityService<Long, U> {
	
	U getByUserName(String userName);
	
	/**
	 * @deprecated use the ISearchQuery pattern instead.
	 */
	@Deprecated
	List<U> search(String searchPattern) throws ServiceException, SecurityServiceException;
	
	/**
	 * @deprecated use the ISearchQuery pattern instead.
	 */
	@Deprecated
	List<U> searchAutocomplete(String string) throws ServiceException, SecurityServiceException;
	
	/**
	 * @deprecated use the ISearchQuery pattern instead.
	 */
	@Deprecated
	<U2 extends U> List<U2> searchAutocomplete(Class<U2> clazz, String searchPattern) throws ServiceException, SecurityServiceException;
	
	/**
	 * @deprecated use the ISearchQuery pattern instead.
	 */
	@Deprecated
	List<U> searchAutocomplete(String string, Integer limit, Integer offset) throws ServiceException, SecurityServiceException;

	/**
	 * @deprecated use the ISearchQuery pattern instead.
	 */
	@Deprecated
	<U2 extends U> List<U2> searchAutocomplete(Class<U2> clazz, String searchPattern, Integer limit, Integer offset)
			throws ServiceException, SecurityServiceException;

	void setActive(U person, boolean active) throws ServiceException, SecurityServiceException;

	Long countActive();

	void setPasswords(U person, String clearTextPassword) throws ServiceException, SecurityServiceException;

	void addAuthority(U person, Authority authority) throws ServiceException, SecurityServiceException;

	void addAuthority(U person, String authorityName) throws ServiceException, SecurityServiceException;

	void updateLastLoginDate(U person) throws ServiceException, SecurityServiceException;
	
	void updateLocale(U person, Locale locale) throws ServiceException, SecurityServiceException;

	void updateProfileInformation(U person) throws ServiceException, SecurityServiceException;

	U getByUserNameCaseInsensitive(String userName);

}
