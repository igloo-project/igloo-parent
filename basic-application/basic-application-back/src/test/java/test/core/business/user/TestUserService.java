package test.core.business.user;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.USER_EDIT_PASSWORD;

import basicapp.back.business.notification.service.exception.NotificationException;
import basicapp.back.business.role.model.Role;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.service.controller.IUserControllerService;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.DateUtil;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import test.core.AbstractBasicApplicationTestCase;
import test.core.config.spring.SpringBootTestBasicApplication;

@SpringBootTestBasicApplication
class TestUserService extends AbstractBasicApplicationTestCase {

  @Autowired private IUserControllerService userControllerService;

  @Nested
  class saveUser {
    @WithUserDetails(value = ADMIN_USERNAME, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testSaveTechnicalUser() throws SecurityServiceException, NotificationException {
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
      User userBdd = userRepository.getReferenceById(user.getId());
      Assertions.assertThat(userBdd.getUsername()).isEqualTo("test");
      Assertions.assertThat(userBdd.getFirstName()).isEqualTo("firstname");
      Assertions.assertThat(userBdd.getLastName()).isEqualTo("lastname");
      Assertions.assertThat(userBdd.getType()).isEqualTo(UserType.TECHNICAL);
      Assertions.assertThat(userBdd.getPasswordHash()).startsWith("{bcrypt}");
      Assertions.assertThat(userBdd.isEnabled()).isTrue();
      Assertions.assertThat(userBdd.getLocale()).isEqualTo(Locale.FRENCH);
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getCreation().getDate());
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getModification().getDate());
      Assertions.assertThat(new Date())
          .isInSameDayAs(userBdd.getPasswordInformation().getLastUpdateDate());
    }

    @WithUserDetails(
        value = BASIC_USERNAME_WITH_PERMISSIONS,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testSaveBasicUser() throws SecurityServiceException, NotificationException {
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
      User userBdd = userRepository.getReferenceById(user.getId());
      Assertions.assertThat(userBdd.getUsername()).isEqualTo("test");
      Assertions.assertThat(userBdd.getFirstName()).isEqualTo("firstname");
      Assertions.assertThat(userBdd.getLastName()).isEqualTo("lastname");
      Assertions.assertThat(userBdd.getType()).isEqualTo(UserType.BASIC);
      Assertions.assertThat(userBdd.getPasswordHash()).startsWith("{bcrypt}");
      Assertions.assertThat(userBdd.isEnabled()).isTrue();
      Assertions.assertThat(userBdd.getLocale()).isEqualTo(Locale.FRENCH);
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getCreation().getDate());
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getModification().getDate());
      Assertions.assertThat(new Date())
          .isInSameDayAs(userBdd.getPasswordInformation().getLastUpdateDate());
    }

    @WithUserDetails(value = ADMIN_USERNAME, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Sql(scripts = {"/scripts/user-test.sql"})
    @Test
    void testUpdateUser() throws SecurityServiceException, NotificationException {
      entityManagerReset();
      User user = userRepository.getReferenceById(-4L);
      user.setFirstName("updatedFirstname");
      user.setLastName("updatedFirstname");
      userControllerService.saveTechnicalUser(user, "newPassword");
      entityManagerReset();
      User userBdd = userRepository.getReferenceById(user.getId());
      Assertions.assertThat(userBdd.getUsername()).isEqualTo("test");
      Assertions.assertThat(userBdd.getFirstName()).isEqualTo("updatedFirstname");
      Assertions.assertThat(userBdd.getLastName()).isEqualTo("updatedFirstname");
      Assertions.assertThat(userBdd.getType()).isEqualTo(UserType.TECHNICAL);
      Assertions.assertThat(userBdd.getPasswordHash()).startsWith("{bcrypt}");
      Assertions.assertThat(userBdd.isEnabled()).isTrue();
      Assertions.assertThat(userBdd.getLocale()).isEqualTo(Locale.FRENCH);
      Assertions.assertThat(DateUtil.parse("2024-01-01"))
          .isInSameDayAs(userBdd.getCreation().getDate());
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getModification().getDate());
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

  @Test
  void testListUser() {
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
    List<User> userList = userRepository.findAll();

    Assertions.assertThat(userList).size().isEqualTo(4);
    User userBdd = userRepository.getReferenceById(user.getId());
    Assertions.assertThat(userBdd.getUsername()).isEqualTo("test");
    Assertions.assertThat(userBdd.getFirstName()).isEqualTo("firstname");
    Assertions.assertThat(userBdd.getLastName()).isEqualTo("lastname");
    Assertions.assertThat(userBdd.getType()).isEqualTo(UserType.BASIC);
    Assertions.assertThat(userBdd.getRoles()).containsExactlyInAnyOrder(role1, role2);
  }
}
