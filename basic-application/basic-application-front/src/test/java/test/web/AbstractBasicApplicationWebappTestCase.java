package test.web;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.business.IUserService;
import basicapp.back.security.service.IBasicApplicationAuthenticationService;
import java.util.Set;
import org.apache.wicket.Localizer;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.test.jpa.junit.SetupAndCleanDatabaseTestExecutionListener;
import org.iglooproject.test.wicket.core.AbstractWicketTestCase;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import test.core.TestEntityDatabaseHelper;

@TestExecutionListeners(
    value = {SetupAndCleanDatabaseTestExecutionListener.class},
    mergeMode =
        TestExecutionListeners.MergeMode
            .MERGE_WITH_DEFAULTS // Retains default TestExecutionListeners.
    )
@Sql(scripts = "/scripts/init-data-wicket-test.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class AbstractBasicApplicationWebappTestCase
    extends AbstractWicketTestCase<BasicApplicationWicketTester> {

  protected static final String USER_PASSWORD = "USER_PASSWORD";

  protected User basicUser;

  protected User administrator;

  @Autowired protected IUserService userService;

  @Autowired protected IBasicApplicationAuthenticationService authenticationService;

  @Autowired protected PasswordEncoder passwordEncoder;

  @Autowired private IMutablePropertyDao mutablePropertyDao;

  @Autowired private WebApplication application;

  @Autowired private TestEntityDatabaseHelper entityDatabaseHelper;

  @BeforeEach
  @Override
  public void init() throws ServiceException, SecurityServiceException {
    cleanAll();
    setWicketTester(new BasicApplicationWicketTester(application));
    initUsers();
  }

  @Override
  @AfterEach
  public void close() {
    // nothing to do
  }

  @Override
  protected void cleanAll() throws ServiceException, SecurityServiceException {
    entityManagerClear();

    mutablePropertyDao.cleanInTransaction();

    authenticationService.signOut();

    emptyIndexes();
  }

  private void initUsers() {
    basicUser = userService.getByUsername("basicUser");
    administrator = userService.getByUsername("technicalUser");
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
