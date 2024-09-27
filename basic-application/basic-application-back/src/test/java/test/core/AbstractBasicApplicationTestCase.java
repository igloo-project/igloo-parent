package test.core;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.business.IUserService;
import java.util.Set;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.iglooproject.test.jpa.junit.SetupAndCleanDatabaseTestExecutionListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

@TestExecutionListeners(
    value = {SetupAndCleanDatabaseTestExecutionListener.class},
    mergeMode =
        TestExecutionListeners.MergeMode
            .MERGE_WITH_DEFAULTS // Retains default TestExecutionListeners.
    )
@Sql(scripts = "/scripts/init-data-test.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class AbstractBasicApplicationTestCase extends AbstractTestCase {

  public static final String BASIC_USERNAME_WITH_PERMISSIONS = "BASIC_USERNAME_WITH_PERMISSIONS";
  public static final String ADMIN_USERNAME = "ADMIN_USERNAME";
  public static final String BASIC_USERNAME_WITHOUT_PERMISSIONS =
      "BASIC_USERNAME_WITHOUT_PERMISSIONS";

  @Autowired protected TestEntityDatabaseHelper entityDatabaseHelper;

  @Autowired protected IUserService userService;

  @BeforeEach
  @Override
  public void init() throws ServiceException, SecurityServiceException {
    clearCaches();
  }

  @AfterEach
  @Override
  public void close() throws ServiceException, SecurityServiceException {
    // Database cleaning performed by CleanDatabaseTestExecutionListener
    // nothing to do
  }

  protected User addPermissions(User user, String... permissions)
      throws ServiceException, SecurityServiceException {
    user.addRole(entityDatabaseHelper.createRole(r -> r.setPermissions(Set.of(permissions)), true));
    userService.update(user);
    return user;
  }
}
