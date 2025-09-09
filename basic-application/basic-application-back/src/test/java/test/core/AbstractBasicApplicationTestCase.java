package test.core;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.repository.IUserRepository;
import java.util.Set;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

@Sql(scripts = "/scripts/init-data-test.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class AbstractBasicApplicationTestCase extends AbstractTestCase {

  public static final String BASIC_USERNAME_WITH_PERMISSIONS = "BASIC_USERNAME_WITH_PERMISSIONS";
  public static final String ADMIN_USERNAME = "ADMIN_USERNAME";
  public static final String BASIC_USERNAME_WITHOUT_PERMISSIONS =
      "BASIC_USERNAME_WITHOUT_PERMISSIONS";

  @Autowired protected TestEntityDatabaseHelper entityDatabaseHelper;

  @Autowired protected IUserRepository userRepository;

  @BeforeEach
  @Override
  public void init() {
    // Database cleaning performed by IglooTestExecutionListener
    // nothing to do
  }

  @AfterEach
  @Override
  public void close() {
    // Database cleaning performed by IglooTestExecutionListener
    // nothing to do
  }

  protected User addPermissions(User user, String... permissions) {
    user.addRole(entityDatabaseHelper.createRole(r -> r.setPermissions(Set.of(permissions)), true));
    userRepository.save(user);
    return user;
  }
}
