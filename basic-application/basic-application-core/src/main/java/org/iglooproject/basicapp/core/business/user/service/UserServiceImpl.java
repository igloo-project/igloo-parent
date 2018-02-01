package org.iglooproject.basicapp.core.business.user.service;

import java.util.List;

import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.basicapp.core.business.history.model.bean.HistoryLogAdditionalInformationBean;
import org.iglooproject.basicapp.core.business.history.service.IHistoryLogService;
import org.iglooproject.basicapp.core.business.user.dao.IUserDao;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.person.service.GenericSimpleUserServiceImpl;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.jpa.util.HibernateUtils;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("personService")
public class UserServiceImpl extends GenericSimpleUserServiceImpl<User> implements IUserService {

	@Autowired
	private IUserDao userDao;

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private IHistoryLogService historyLogService;
	
//	@Autowired
//	private IUserDifferenceService userDifferenceService;

	@Autowired
	public UserServiceImpl(IUserDao userDao) {
		super(userDao);
		this.userDao = userDao;
	}

	@Override
	public List<User> listByUsername(String username) {
		return userDao.listByUsername(username);
	}

	@Override
	public void onSignIn(User user) throws ServiceException, SecurityServiceException {
		historyLogService.log(HistoryEventType.SIGN_IN, user, HistoryLogAdditionalInformationBean.empty());
	}

	@Override
	public void onSignInFail(User user) throws ServiceException, SecurityServiceException {
		historyLogService.log(HistoryEventType.SIGN_IN_FAIL, user, HistoryLogAdditionalInformationBean.empty());
	}

	@Override
	public void onCreate(User user, User author) throws ServiceException, SecurityServiceException {
		historyLogService.log(HistoryEventType.CREATE, user, HistoryLogAdditionalInformationBean.empty());
	}
	
	@Override
	public void setActive(User person, boolean active) throws ServiceException, SecurityServiceException {
		super.setActive(person, active);
		historyLogService.log(active ? HistoryEventType.ENABLE : HistoryEventType.DISABLE, person, HistoryLogAdditionalInformationBean.empty());
	}
	
	@Override
	protected void updateEntity(User person) throws ServiceException, SecurityServiceException { // NOSONAR
		super.updateEntity(person);
		
//		historyLogService.logWithDifferences(HistoryEventType.UPDATE, person, HistoryLogObjectsBean.of(person),
//				userDifferenceService.getMinimalDifferenceGenerator(),
//				userDifferenceService);
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
		String username = authenticationService.getUsername();
		if (username == null) {
			return null;
		}
		
		return HibernateUtils.unwrap(getByUsername(username));
	}

}
