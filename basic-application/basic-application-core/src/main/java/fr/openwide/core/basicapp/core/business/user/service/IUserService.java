package fr.openwide.core.basicapp.core.business.user.service;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserSearchParameters;
import fr.openwide.core.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import fr.openwide.core.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import fr.openwide.core.basicapp.core.business.user.model.embeddable.UserPasswordRecoveryRequest;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.service.IGenericUserService;

public interface IUserService extends IGenericUserService<User> {

	List<User> listByUserName(String userName);

	<U extends User> List<U> search(Class<U> clazz, UserSearchParameters searchParams, Integer limit, Integer offset) throws ParseException;
	
	<U extends User> int count(Class<U> clazz, UserSearchParameters searchParams) throws ParseException;

	UserPasswordRecoveryRequest initiatePasswordRecoveryRequest(User user, UserPasswordRecoveryRequestType type,
			UserPasswordRecoveryRequestInitiator initiator) throws ServiceException, SecurityServiceException;

	User getByEmailCaseInsensitive(String email);

}