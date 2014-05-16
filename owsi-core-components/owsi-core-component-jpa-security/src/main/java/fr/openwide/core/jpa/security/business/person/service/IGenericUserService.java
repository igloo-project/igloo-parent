package fr.openwide.core.jpa.security.business.person.service;

import java.util.List;
import java.util.Locale;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;

public interface IGenericUserService<U extends GenericUser<U, ?>> extends IGenericEntityService<Long, U> {
	
	U getByUserName(String userName);
	
	List<U> search(String searchPattern) throws ServiceException, SecurityServiceException;
	
	List<U> searchAutocomplete(String string) throws ServiceException, SecurityServiceException;
	
	<U2 extends U> List<U2> searchAutocomplete(Class<U2> clazz, String searchPattern) throws ServiceException, SecurityServiceException;
	
	List<U> searchAutocomplete(String string, Integer limit, Integer offset) throws ServiceException, SecurityServiceException;

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
