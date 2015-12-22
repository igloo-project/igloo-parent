package fr.openwide.core.basicapp.core.business.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.history.model.atomic.HistoryEventType;
import fr.openwide.core.basicapp.core.business.history.model.bean.HistoryLogObjectsBean;
import fr.openwide.core.basicapp.core.business.history.service.IHistoryLogService;
import fr.openwide.core.basicapp.core.business.user.dao.IUserDao;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.service.GenericSimpleUserServiceImpl;
import fr.openwide.core.jpa.security.service.IAuthenticationService;
import fr.openwide.core.jpa.util.HibernateUtils;
import fr.openwide.core.spring.util.StringUtils;

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
	public List<User> listByUserName(String userName) {
		return userDao.listByUserName(userName);
	}

	@Override
	public void onSignIn(User user) throws ServiceException, SecurityServiceException {
		historyLogService.log(HistoryEventType.SIGN_IN, HistoryLogObjectsBean.of(user));
	}

	@Override
	public void onSignInFail(User user) throws ServiceException, SecurityServiceException {
		historyLogService.log(HistoryEventType.SIGN_IN_FAIL, HistoryLogObjectsBean.of(user));
	}

	@Override
	public void onCreate(User user, User author) throws ServiceException, SecurityServiceException {
		historyLogService.log(HistoryEventType.CREATE, HistoryLogObjectsBean.of(user));
	}
	
	@Override
	public void setActive(User person, boolean active) throws ServiceException, SecurityServiceException {
		super.setActive(person, active);
		historyLogService.log(active ? HistoryEventType.ENABLE : HistoryEventType.DISABLE, HistoryLogObjectsBean.of(person));
	}
	
	@Override
	protected void updateEntity(User person) throws ServiceException, SecurityServiceException {
		super.updateEntity(person);
		
//		historyLogService.logWithDifferences(HistoryEventType.UPDATE, HistoryLogObjectsBean.of(person),
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
		String userName = authenticationService.getUserName();
		if (userName == null) {
			return null;
		}
		
		return HibernateUtils.unwrap(getByUserName(userName));
	}

}
