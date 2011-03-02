package fr.openwide.core.hibernate.security.business.person.service;

import java.util.List;

import fr.openwide.core.hibernate.business.generic.service.GenericEntityService;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.core.hibernate.security.business.authority.model.Authority;
import fr.openwide.core.hibernate.security.business.person.model.CorePerson;

public interface PersonService extends GenericEntityService<Integer, CorePerson> {
	
	CorePerson getByUserName(String userName);
	
	List<CorePerson> search(String searchPattern) throws ServiceException, SecurityServiceException;
	
	List<CorePerson> searchAutocomplete(String string) throws ServiceException, SecurityServiceException;

	void setActive(CorePerson person, boolean active) throws ServiceException, SecurityServiceException;

	Long countActive();

	void setPasswords(CorePerson person, String clearTextPassword) throws ServiceException, SecurityServiceException;

	boolean comparePasswordToMd5Passwords(CorePerson person, String clearTextPassword);

	void addAuthority(CorePerson person, Authority authority) throws ServiceException, SecurityServiceException;

	void addAuthority(CorePerson person, String authorityName) throws ServiceException, SecurityServiceException;

	void updateLastLoginDate(CorePerson person) throws ServiceException, SecurityServiceException;

	void updateProfileInformation(CorePerson person) throws ServiceException, SecurityServiceException;

}
