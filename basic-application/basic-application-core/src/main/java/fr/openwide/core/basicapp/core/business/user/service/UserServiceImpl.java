package fr.openwide.core.basicapp.core.business.user.service;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.audit.model.atomic.AuditAction;
import fr.openwide.core.basicapp.core.business.audit.service.IAuditService;
import fr.openwide.core.basicapp.core.business.notification.service.INotificationService;
import fr.openwide.core.basicapp.core.business.user.dao.IUserDao;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserSearchParameters;
import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.service.GenericSimpleUserServiceImpl;
import fr.openwide.core.jpa.security.service.IAuthenticationService;
import fr.openwide.core.spring.util.StringUtils;

@Service("personService")
public class UserServiceImpl extends GenericSimpleUserServiceImpl<User> implements IUserService {

	private static final String AUDIT_SIGN_IN_METHOD_NAME = "signIn";

	private static final String AUDIT_SIGN_IN_FAIL_METHOD_NAME = "signInFail";

	private static final String AUDIT_CREATE_METHOD_NAME = "create";

	@Autowired
	private IUserDao userDao;

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private IAuditService auditService;

	@Autowired
	private BasicApplicationConfigurer configurer;

	@Autowired
	private INotificationService notificationService;

	@Autowired
	private ISecurityManagementService securityManagementService;

	@Autowired
	public UserServiceImpl(IUserDao userDao) {
		super(userDao);
		this.userDao = userDao;
	}

	private void audit(User object, AuditAction action, String methodName) throws ServiceException, SecurityServiceException {
		auditService.audit(getClass().getSimpleName(), methodName, object, action);
	}

	private void audit(User subject, User object, AuditAction action, String methodName) throws ServiceException, SecurityServiceException {
		auditService.audit(getClass().getSimpleName(), methodName, subject, object, action);
	}

	@Override
	public List<User> listByUserName(String userName) {
		return userDao.listByUserName(userName);
	}

	@Override
	public <U extends User> List<U> search(Class<U> clazz, UserSearchParameters searchParameters, Integer limit, Integer offset) throws ParseException {
		return userDao.search(clazz, searchParameters, limit, offset);
	}

	@Override
	public <U extends User> int count(Class<U> clazz, UserSearchParameters searchParameters) throws ParseException {
		return userDao.count(clazz, searchParameters);
	}

	@Override
	public void onSignIn(User user) throws ServiceException, SecurityServiceException {
		audit(user, AuditAction.SIGN_IN, AUDIT_SIGN_IN_METHOD_NAME);
	}

	@Override
	public void onSignInFail(User user) throws ServiceException, SecurityServiceException {
		audit(user, user, AuditAction.SIGN_IN_FAIL, AUDIT_SIGN_IN_FAIL_METHOD_NAME);
	}

	@Override
	public void onCreate(User user, User author) throws ServiceException, SecurityServiceException {
		audit(author, user, AuditAction.CREATE_USER, AUDIT_CREATE_METHOD_NAME);
	}

	@Override
	public User getByEmailCaseInsensitive(String email) {
		if (!StringUtils.hasText(StringUtils.lowerCase(email))) {
			return null;
		}
		return userDao.getByEmailCaseInsensitive(email);
	}

	@Override
	public User getAuthenticatedUser() {
		String userName = authenticationService.getUserName();
		if (userName == null) {
			return null;
		}
		
		return getByUserName(userName);
	}

}
