package fr.openwide.core.basicapp.core.business.user.service;

import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserSearchParameters;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.service.IGenericUserService;

public interface IUserService extends IGenericUserService<User> {

	List<User> listByUserName(String userName);

	<U extends User> List<U> search(Class<U> clazz, UserSearchParameters searchParams, Integer limit, Integer offset) throws ParseException;
	
	<U extends User> int count(Class<U> clazz, UserSearchParameters searchParams) throws ParseException;

	void onSignIn(User user) throws ServiceException, SecurityServiceException;

	void onSignInFail(User user) throws ServiceException, SecurityServiceException;

	void onCreate(User user, User subject) throws ServiceException, SecurityServiceException;

	User getByEmailCaseInsensitive(String email);

	User getAuthenticatedUser();

}