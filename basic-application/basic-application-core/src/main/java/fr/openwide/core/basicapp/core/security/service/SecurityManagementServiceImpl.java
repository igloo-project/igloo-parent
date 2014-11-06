package fr.openwide.core.basicapp.core.security.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.openwide.core.basicapp.core.business.audit.model.AuditActionType;
import fr.openwide.core.basicapp.core.business.audit.service.IAuditService;
import fr.openwide.core.basicapp.core.business.notification.service.INotificationService;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import fr.openwide.core.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.core.security.model.SecurityOptions;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;
import fr.openwide.core.jpa.util.HibernateUtils;
import fr.openwide.core.spring.util.StringUtils;

public class SecurityManagementServiceImpl implements ISecurityManagementService {

	private static final String AUDIT_INITIATE_PASSWORD_RECOVERY_REQUEST_METHOD_NAME = "initiatePasswordRecoveryRequest";

	private static final String AUDIT_UPDATE_PASSWORD_METHOD_NAME = "updatePassword";

	private static Map<Class<? extends GenericUser<?, ?>>, SecurityOptions> OPTIONS_BY_USER = Maps.newHashMap();

	private static SecurityOptions DEFAULT_OPTIONS = SecurityOptions.DEFAULT;

	@Autowired
	private IUserService userService;

	@Autowired
	private INotificationService notificationService;

	@Autowired
	private IAuditService auditService;

	@Autowired
	private BasicApplicationConfigurer configurer;

	private void audit(User subject, User object, AuditActionType type, String methodName) throws ServiceException, SecurityServiceException {
		auditService.audit(getClass().getSimpleName(), methodName, subject, object, type);
	}

	public SecurityManagementServiceImpl setOptions(Class<? extends User> clazz, SecurityOptions options) {
		checkNotNull(clazz);
		checkNotNull(options);
		
		OPTIONS_BY_USER.put(clazz, options);
		
		return this;
	}

	public SecurityManagementServiceImpl setDefaultOptions(SecurityOptions options) {
		checkNotNull(options);
		
		DEFAULT_OPTIONS = options;
		
		return this;
	}

	@Override
	public SecurityOptions getOptions(Class<? extends User> clazz) {
		if (OPTIONS_BY_USER.containsKey(clazz)) {
			return OPTIONS_BY_USER.get(clazz);
		}
		return DEFAULT_OPTIONS;
	}

	@Override
	public SecurityOptions getOptions(User user) {
		if (user == null) {
			return DEFAULT_OPTIONS;
		}
		return getOptions(HibernateUtils.unwrap(user).getClass());
	}

	@Override
	public void initiatePasswordRecoveryRequest(User user, UserPasswordRecoveryRequestType type,
			UserPasswordRecoveryRequestInitiator initiator) throws ServiceException, SecurityServiceException {
		initiatePasswordRecoveryRequest(user, type, initiator, user);
	}

	@Override
	public void initiatePasswordRecoveryRequest(User user, UserPasswordRecoveryRequestType type,
			UserPasswordRecoveryRequestInitiator initiator, User author) throws ServiceException, SecurityServiceException {
		Date now = new Date();
		
		user.getPasswordRecoveryRequest().setToken(getPasswordChangeRequestToken(user, now));
		user.getPasswordRecoveryRequest().setCreationDate(now);
		user.getPasswordRecoveryRequest().setType(type);
		user.getPasswordRecoveryRequest().setInitiator(initiator);
		
		userService.update(user);
		
		notificationService.sendUserPasswordRecoveryRequest(user);
		
		switch (type) {
		case CREATION:
			audit(author, user, AuditActionType.PASSWORD_CREATION_REQUEST, AUDIT_INITIATE_PASSWORD_RECOVERY_REQUEST_METHOD_NAME);
			break;
		case RESET:
			audit(author, user, AuditActionType.PASSWORD_RESET_REQUEST, AUDIT_INITIATE_PASSWORD_RECOVERY_REQUEST_METHOD_NAME);
			break;
		default:
			break;
		}
	}

	private String getPasswordChangeRequestToken(User user, Date date) {
		return DigestUtils.sha256Hex(String.format("%1$s - %2$s - %3$s", user.getId(),
				configurer.getSecurityPasswordRecoveryRequestTokenSalt(), date));
	}

	@Override
	public boolean isPasswordExpired(User user) {
		if (user == null
				|| user.getPasswordInformation().getLastUpdateDate() == null
				|| !getOptions(user).isPasswordExpirationEnabled()) {
			return false;
		}
		
		Date expirationDate = DateUtils.addDays(user.getPasswordInformation().getLastUpdateDate(), configurer.getSecurityPasswordExpirationDays());
		Date now = new Date();
		
		return now.after(expirationDate);
	}

	@Override
	public boolean isPasswordRecoveryRequestExpired(User user) {
		if (user == null
				|| user.getPasswordRecoveryRequest().getToken() == null
				|| user.getPasswordRecoveryRequest().getCreationDate() == null) {
			return true;
		}
		
		Date expirationDate = DateUtils.addMinutes(user.getPasswordRecoveryRequest().getCreationDate(), configurer.getSecurityPasswordRecoveryRequestExpirationMinutes());
		Date now = new Date();
		
		return now.after(expirationDate);
	}

	@Override
	public void updatePassword(User user, String password) throws ServiceException, SecurityServiceException {
		updatePassword(user, password, user);
	}

	@Override
	public void updatePassword(User user, String password, User author) throws ServiceException, SecurityServiceException {
		if (user == null || !StringUtils.hasText(password)) {
			return;
		}
		
		userService.setPasswords(user, password);
		user.getPasswordInformation().setLastUpdateDate(new Date());
		
		if (getOptions(user).isPasswordHistoryEnabled()) {
			EvictingQueue<String> historyQueue = EvictingQueue.create(configurer.getSecurityPasswordHistoryCount());
			
			for (String oldPassword : user.getPasswordInformation().getHistory()) {
				historyQueue.offer(oldPassword);
			}
			historyQueue.offer(user.getPasswordHash());
			
			user.getPasswordInformation().setHistory(Lists.newArrayList(historyQueue));
		}
		
		user.getPasswordRecoveryRequest().reset();
		userService.update(user);
		
		audit(author, user, AuditActionType.PASSWORD_UPDATE, AUDIT_UPDATE_PASSWORD_METHOD_NAME);
	}

}
