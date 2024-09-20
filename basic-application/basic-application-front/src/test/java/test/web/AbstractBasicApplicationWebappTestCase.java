package test.web;

import basicapp.back.business.history.service.IHistoryLogService;
import basicapp.back.business.role.service.IRoleService;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.service.business.IUserService;
import basicapp.back.security.service.IBasicApplicationAuthenticationService;
import java.util.Set;
import org.apache.wicket.Localizer;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.test.wicket.core.AbstractWicketTestCase;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.core.TestEntityDatabaseHelper;

public abstract class AbstractBasicApplicationWebappTestCase
    extends AbstractWicketTestCase<BasicApplicationWicketTester> {

  protected static final String USER_PASSWORD = "USER_PASSWORD";

  protected User basicUser;

  protected User administrator;

  private static int userUniqueToken = 0;

  @Autowired protected IUserService userService;

  @Autowired protected IAuthorityService authorityService;

  @Autowired protected IBasicApplicationAuthenticationService authenticationService;

  @Autowired protected IHistoryLogService historyLogService;

  @Autowired protected PasswordEncoder passwordEncoder;

  @Autowired private IMutablePropertyDao mutablePropertyDao;

  @Autowired private WebApplication application;

  @Autowired private TestEntityDatabaseHelper entityDatabaseHelper;

  @Autowired private IRoleService roleService;

  @BeforeEach
  public void setUp() throws ServiceException, SecurityServiceException {
    initAuthorities();
    initUsers();

    setWicketTester(new BasicApplicationWicketTester(application));
  }

  @Override
  protected void cleanAll() throws ServiceException, SecurityServiceException {
    entityManagerClear();

    cleanEntities(userService);
    cleanEntities(authorityService);
    cleanEntities(historyLogService);
    cleanEntities(roleService);

    mutablePropertyDao.cleanInTransaction();

    authenticationService.signOut();

    emptyIndexes();
  }

  private void initAuthorities() throws ServiceException, SecurityServiceException {
    authorityService.create(new Authority(CoreAuthorityConstants.ROLE_ADMIN));
    authorityService.create(new Authority(CoreAuthorityConstants.ROLE_AUTHENTICATED));
  }

  private void initUsers() throws ServiceException, SecurityServiceException {
    basicUser =
        entityDatabaseHelper.createUser(
            u -> {
              u.setType(UserType.BASIC);
              u.setUsername("basicUser");
            },
            true);

    entityDatabaseHelper.createUser(
        u -> {
          u.setType(UserType.BASIC);
          u.setUsername("basicUser2");
        },
        true);

    administrator = entityDatabaseHelper.createUser(u -> u.setUsername("technicalUser"), true);
  }

  protected void addPermissions(User user, String... permissions)
      throws ServiceException, SecurityServiceException {
    user.addRole(entityDatabaseHelper.createRole(r -> r.setPermissions(Set.of(permissions)), true));
    userService.update(user);
  }

  protected void authenticateUser(User user) throws ServiceException, SecurityServiceException {
    if (AuthenticatedWebSession.exists()) {
      AuthenticatedWebSession.get().invalidate();
    }

    AbstractCoreSession<?> session = AbstractCoreSession.get();
    User loggedInUser = null;

    session.signIn(user.getUsername(), USER_PASSWORD);
    loggedInUser = (User) session.getUser();
    userService.onSignIn(loggedInUser);
  }

  // Depends on the tester.getSession.getLocale()
  protected static String localize(String key) {
    return Localizer.get().getString(key, null);
  }
}
