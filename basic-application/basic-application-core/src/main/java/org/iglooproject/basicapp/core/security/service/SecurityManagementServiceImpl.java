package org.iglooproject.basicapp.core.security.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_EXPIRATION_DAYS;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_HISTORY_COUNT;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.basicapp.core.business.history.model.bean.HistoryLogAdditionalInformationBean;
import org.iglooproject.basicapp.core.business.history.service.IHistoryLogService;
import org.iglooproject.basicapp.core.business.notification.service.INotificationService;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.security.model.SecurityOptions;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.person.model.GenericUser;
import org.iglooproject.jpa.util.HibernateUtils;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SecurityManagementServiceImpl implements ISecurityManagementService {

	private final Map<Class<? extends GenericUser<?, ?>>, SecurityOptions> optionsByUser = Maps.newHashMap();

	private SecurityOptions defaultOptions = SecurityOptions.DEFAULT;

	@Autowired
	private IUserService userService;

	@Autowired
	private INotificationService notificationService;

	@Autowired
	private IPropertyService propertyService;
	
	@Autowired
	private IHistoryLogService historyLogService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public SecurityManagementServiceImpl setOptions(Class<? extends User> clazz, SecurityOptions options) {
		checkNotNull(clazz);
		checkNotNull(options);
		
		optionsByUser.put(clazz, options);
		
		return this;
	}

	public SecurityManagementServiceImpl setDefaultOptions(SecurityOptions options) {
		checkNotNull(options);
		
		defaultOptions = options;
		
		return this;
	}

	@Override
	public SecurityOptions getOptions(Class<? extends User> clazz) {
		if (optionsByUser.containsKey(clazz)) {
			return optionsByUser.get(clazz);
		}
		return defaultOptions;
	}

	@Override
	public SecurityOptions getOptions(User user) {
		if (user == null) {
			return defaultOptions;
		}
		return getOptions(HibernateUtils.unwrap(user).getClass());
	}

	@Override
	public void initiatePasswordRecoveryRequest(
		User user,
		UserPasswordRecoveryRequestType type,
		UserPasswordRecoveryRequestInitiator initiator
	) throws ServiceException, SecurityServiceException {
		initiatePasswordRecoveryRequest(user, type, initiator, user);
	}

	@Override
	public void initiatePasswordRecoveryRequest(
		User user,
		UserPasswordRecoveryRequestType type,
		UserPasswordRecoveryRequestInitiator initiator,
		User author
	) throws ServiceException, SecurityServiceException {
		Date now = new Date();
		
		user.getPasswordRecoveryRequest().setToken(RandomStringUtils.randomAlphanumeric(propertyService.get(PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT)));
		user.getPasswordRecoveryRequest().setCreationDate(now);
		user.getPasswordRecoveryRequest().setType(type);
		user.getPasswordRecoveryRequest().setInitiator(initiator);
		
		userService.update(user);
		
		notificationService.sendUserPasswordRecoveryRequest(user);
		
		switch (type) {
		case CREATION:
			historyLogService.log(HistoryEventType.PASSWORD_CREATION_REQUEST, user, HistoryLogAdditionalInformationBean.empty());
			break;
		case RESET:
			historyLogService.log(HistoryEventType.PASSWORD_RESET_REQUEST, user, HistoryLogAdditionalInformationBean.empty());
			break;
		default:
			break;
		}
	}

	@Override
	public boolean isPasswordExpired(User user) {
		if (user == null
				|| user.getPasswordInformation().getLastUpdateDate() == null
				|| !getOptions(user).isPasswordExpirationEnabled()) {
			return false;
		}
		
		Date expirationDate = DateUtils.addDays(user.getPasswordInformation().getLastUpdateDate(), propertyService.get(PASSWORD_EXPIRATION_DAYS));
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
		
		Date expirationDate = DateUtils.addMinutes(user.getPasswordRecoveryRequest().getCreationDate(), propertyService.get(PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES));
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
			EvictingQueue<String> historyQueue = EvictingQueue.create(propertyService.get(PASSWORD_HISTORY_COUNT));
			
			for (String oldPassword : user.getPasswordInformation().getHistory()) {
				historyQueue.offer(oldPassword);
			}
			historyQueue.offer(user.getPasswordHash());
			
			user.getPasswordInformation().setHistory(Lists.newArrayList(historyQueue));
		}
		
		user.getPasswordRecoveryRequest().reset();
		userService.update(user);
		
		historyLogService.log(HistoryEventType.PASSWORD_UPDATE, user, HistoryLogAdditionalInformationBean.empty());
	}

	@Override
	public boolean checkPassword(String password, User user) throws ServiceException, SecurityServiceException {
		return passwordEncoder.matches(password, user.getPasswordHash());
	}

}
