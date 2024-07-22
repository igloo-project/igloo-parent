package basicapp.back.business.user.service;

import basicapp.back.business.history.model.atomic.HistoryEventType;
import basicapp.back.business.history.model.bean.HistoryLogAdditionalInformationBean;
import basicapp.back.business.history.service.IHistoryLogService;
import basicapp.back.business.user.dao.IUserDao;
import basicapp.back.business.user.model.User;
import basicapp.back.security.service.IBasicApplicationAuthenticationService;
import java.util.List;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.service.GenericSimpleUserServiceImpl;
import org.iglooproject.jpa.util.HibernateUtils;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class UserServiceImpl extends GenericSimpleUserServiceImpl<User> implements IUserService {

  @Autowired private IUserDao userDao;

  @Autowired private IBasicApplicationAuthenticationService authenticationService;

  @Autowired private IHistoryLogService historyLogService;

  @Autowired private IPropertyService propertyService;

  //	@Autowired
  //	private IUserDifferenceService userDifferenceService;

  @Autowired
  public UserServiceImpl(IUserDao userDao) {
    super(userDao);
    this.userDao = userDao;
  }

  @Override
  protected void createEntity(User user) throws ServiceException, SecurityServiceException {
    if (user.getLocale() == null) {
      user.setLocale(propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
    }

    super.createEntity(user);
  }

  @Override
  protected void updateEntity(User user) throws ServiceException, SecurityServiceException {
    if (user.getLocale() == null) {
      user.setLocale(propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
    }

    super.updateEntity(user);

    //		historyLogService.logWithDifferences(HistoryEventType.UPDATE, user,
    // HistoryLogObjectsBean.of(user),
    //			userDifferenceService.getMinimalDifferenceGenerator(),
    //			userDifferenceService);
  }

  @Override
  public List<User> listByUsername(String username) {
    return userDao.listByUsername(username);
  }

  @Override
  public void onSignIn(User user) throws ServiceException, SecurityServiceException {
    historyLogService.log(
        HistoryEventType.SIGN_IN, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void onSignInFail(User user) throws ServiceException, SecurityServiceException {
    historyLogService.log(
        HistoryEventType.SIGN_IN_FAIL, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void onCreate(User user, User author) throws ServiceException, SecurityServiceException {
    historyLogService.log(
        HistoryEventType.CREATE, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void setEnabled(User person, boolean enabled)
      throws ServiceException, SecurityServiceException {
    super.setEnabled(person, enabled);
    historyLogService.log(
        enabled ? HistoryEventType.ENABLE : HistoryEventType.DISABLE,
        person,
        HistoryLogAdditionalInformationBean.empty());
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
