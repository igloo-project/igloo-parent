package fr.openwide.core.jpa.security.business.person.service;

import java.util.List;
import java.util.Locale;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;

public interface IPersonService<P extends AbstractPerson<P>> extends IGenericEntityService<Long, P> {
	
	P getByUserName(String userName);
	
	List<P> search(String searchPattern) throws ServiceException, SecurityServiceException;
	
	List<P> searchAutocomplete(String string) throws ServiceException, SecurityServiceException;

	void setActive(P person, boolean active) throws ServiceException, SecurityServiceException;

	Long countActive();

	void setPasswords(P person, String clearTextPassword) throws ServiceException, SecurityServiceException;

	void addAuthority(P person, Authority authority) throws ServiceException, SecurityServiceException;

	void addAuthority(P person, String authorityName) throws ServiceException, SecurityServiceException;

	void updateLastLoginDate(P person) throws ServiceException, SecurityServiceException;
	
	void updateLocale(P person, Locale locale) throws ServiceException, SecurityServiceException;

	void updateProfileInformation(P person) throws ServiceException, SecurityServiceException;

	P getByUserNameCaseInsensitive(String userName);

}
