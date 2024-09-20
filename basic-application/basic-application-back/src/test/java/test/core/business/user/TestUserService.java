package test.core.business.user;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_USER_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_USER_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_EDIT_PASSWORD;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.service.controller.IUserControllerService;
import com.google.common.collect.ImmutableSortedSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.DateUtil;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import test.core.AbstractBasicApplicationTestCase;
import test.core.config.spring.SpringBootTestBasicApplication;

@SpringBootTestBasicApplication
class TestUserService extends AbstractBasicApplicationTestCase {

  private static final String BASIC_USERNAME_WITH_PERMISSIONS = "BASIC_USERNAME_WITH_PERMISSIONS";
  private static final String ADMIN_USERNAME = "ADMIN_USERNAME";
  private static final String BASIC_USERNAME_WITHOUT_PERMISSIONS =
      "BASIC_USERNAME_WITHOUT_PERMISSIONS";

  @Autowired private IUserControllerService userControllerService;

  @Override
  @BeforeEach
  public void init() throws SecurityServiceException, ServiceException {
    super.init();
    entityDatabaseHelper.createUser(
        u -> {
          u.setUsername(BASIC_USERNAME_WITHOUT_PERMISSIONS);
          u.setType(UserType.BASIC);
        },
        true);
    entityDatabaseHelper.createUser(u -> u.setUsername(ADMIN_USERNAME), true);
    addPermissions(
        entityDatabaseHelper.createUser(
            u -> {
              u.setUsername(BASIC_USERNAME_WITH_PERMISSIONS);
              u.setType(UserType.BASIC);
            },
            true),
        GLOBAL_USER_WRITE,
        GLOBAL_USER_READ);
  }

  @Test
  void testListUser() throws ServiceException, SecurityServiceException {
    Role role1 =
        entityDatabaseHelper.createRole(
            r ->
                r.setPermissions(
                    ImmutableSortedSet.of(GLOBAL_REFERENCE_DATA_READ, GLOBAL_REFERENCE_DATA_WRITE)),
            true);

    Role role2 =
        entityDatabaseHelper.createRole(
            r -> r.setPermissions(ImmutableSortedSet.of(GLOBAL_ROLE_WRITE, GLOBAL_ROLE_READ)),
            true);

    User user =
        entityDatabaseHelper.createUser(
            u -> {
              u.setUsername("test");
              u.setFirstName("firstname");
              u.setLastName("lastname");
              u.setType(UserType.BASIC);
              u.setRoles(ImmutableSortedSet.of(role1, role2));
            },
            true);

    entityManagerReset();
    List<User> userList = userService.list();

    Assertions.assertThat(userList).size().isEqualTo(4);
    User userBdd = userService.getById(user.getId());
    Assertions.assertThat(userBdd.getUsername()).isEqualTo("test");
    Assertions.assertThat(userBdd.getFirstName()).isEqualTo("firstname");
    Assertions.assertThat(userBdd.getLastName()).isEqualTo("lastname");
    Assertions.assertThat(userBdd.getType()).isEqualTo(UserType.BASIC);
    Assertions.assertThat(userBdd.getRoles()).containsExactlyInAnyOrder(role1, role2);
  }

  @Nested
  class saveUser {
    @WithUserDetails(value = ADMIN_USERNAME, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testSaveTechnicalUser() throws SecurityServiceException, ServiceException {
      User user =
          entityDatabaseHelper.createUser(
              u -> {
                u.setUsername("test");
                u.setFirstName("firstname");
                u.setLastName("lastname");
              },
              false);

      userControllerService.saveTechnicalUser(user, USER_EDIT_PASSWORD);
      entityManagerReset();
      User userBdd = userService.getById(user.getId());
      Assertions.assertThat(userBdd.getUsername()).isEqualTo("test");
      Assertions.assertThat(userBdd.getFirstName()).isEqualTo("firstname");
      Assertions.assertThat(userBdd.getLastName()).isEqualTo("lastname");
      Assertions.assertThat(userBdd.getType()).isEqualTo(UserType.TECHNICAL);
      Assertions.assertThat(userBdd.getPasswordHash()).startsWith("{bcrypt}");
      Assertions.assertThat(userBdd.isEnabled()).isTrue();
      Assertions.assertThat(userBdd.getLocale()).isEqualTo(Locale.FRENCH);
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getCreationDate());
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getLastUpdateDate());
      Assertions.assertThat(new Date())
          .isInSameDayAs(userBdd.getPasswordInformation().getLastUpdateDate());
    }

    @WithUserDetails(
        value = BASIC_USERNAME_WITH_PERMISSIONS,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testSaveBasicUser() throws SecurityServiceException, ServiceException {
      User user =
          entityDatabaseHelper.createUser(
              u -> {
                u.setUsername("test");
                u.setFirstName("firstname");
                u.setLastName("lastname");
                u.setType(null);
              },
              false);

      userControllerService.saveBasicUser(user, USER_EDIT_PASSWORD);
      entityManagerReset();
      User userBdd = userService.getById(user.getId());
      Assertions.assertThat(userBdd.getUsername()).isEqualTo("test");
      Assertions.assertThat(userBdd.getFirstName()).isEqualTo("firstname");
      Assertions.assertThat(userBdd.getLastName()).isEqualTo("lastname");
      Assertions.assertThat(userBdd.getType()).isEqualTo(UserType.BASIC);
      Assertions.assertThat(userBdd.getPasswordHash()).startsWith("{bcrypt}");
      Assertions.assertThat(userBdd.isEnabled()).isTrue();
      Assertions.assertThat(userBdd.getLocale()).isEqualTo(Locale.FRENCH);
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getCreationDate());
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getLastUpdateDate());
      Assertions.assertThat(new Date())
          .isInSameDayAs(userBdd.getPasswordInformation().getLastUpdateDate());
    }

    @WithUserDetails(value = ADMIN_USERNAME, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testUpdateUser() throws SecurityServiceException, ServiceException {
      User user =
          entityDatabaseHelper.createUser(
              u -> {
                u.setUsername("test");
                u.setFirstName("firstname");
                u.setLastName("lastname");
              },
              true);
      user.setCreationDate(
          LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
      user.setLastUpdateDate(
          LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
      user.getPasswordInformation()
          .setLastUpdateDate(
              LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

      userService.flush();
      entityManagerReset();
      User userToUpdate = userService.getById(user.getId());
      userToUpdate.setFirstName("updatedFirstname");
      userToUpdate.setLastName("updatedFirstname");
      userControllerService.saveTechnicalUser(userToUpdate, "newPassword");
      entityManagerReset();
      User userBdd = userService.getById(user.getId());
      Assertions.assertThat(userBdd.getUsername()).isEqualTo("test");
      Assertions.assertThat(userBdd.getFirstName()).isEqualTo("updatedFirstname");
      Assertions.assertThat(userBdd.getLastName()).isEqualTo("updatedFirstname");
      Assertions.assertThat(userBdd.getType()).isEqualTo(UserType.TECHNICAL);
      Assertions.assertThat(userBdd.getPasswordHash()).startsWith("{bcrypt}");
      Assertions.assertThat(userBdd.isEnabled()).isTrue();
      Assertions.assertThat(userBdd.getLocale()).isEqualTo(Locale.FRENCH);
      Assertions.assertThat(DateUtil.parse("2024-01-01")).isInSameDayAs(userBdd.getCreationDate());
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getLastUpdateDate());
    }

    @WithUserDetails(
        value = BASIC_USERNAME_WITH_PERMISSIONS,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testSaveTechnicalUser_userBasicAuthenticate_throwAuthorizationDeniedException() {
      Assertions.assertThatThrownBy(
              () ->
                  userControllerService.saveTechnicalUser(
                      entityDatabaseHelper.createUser(null, false), USER_EDIT_PASSWORD))
          .isInstanceOf(AuthorizationDeniedException.class);
    }

    @WithUserDetails(value = ADMIN_USERNAME, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testSaveBasicUser_adminUserAuthenticate_doesNotThrowException() {
      Assertions.assertThatCode(
              () ->
                  userControllerService.saveBasicUser(
                      entityDatabaseHelper.createUser(null, false), USER_EDIT_PASSWORD))
          .doesNotThrowAnyException();
    }

    @WithUserDetails(
        value = BASIC_USERNAME_WITHOUT_PERMISSIONS,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testSaveBasicUser_userWithoutPermissionsAuthenticate_throwAuthorizationDeniedException() {
      Assertions.assertThatThrownBy(
              () ->
                  userControllerService.saveBasicUser(
                      entityDatabaseHelper.createUser(null, false), USER_EDIT_PASSWORD))
          .isInstanceOf(AuthorizationDeniedException.class);
    }
  }
}
