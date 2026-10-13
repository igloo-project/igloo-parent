package test.core.business.user;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_USER_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_USER_WRITE;
import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.security.model.SecurityOptions;
import basicapp.back.security.service.ISecurityManagementService;
import basicapp.back.security.service.permission.UserPermissionEvaluatorImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestUserPermissions {

  @Spy @InjectMocks private UserPermissionEvaluatorImpl userPermissionEvaluator;

  @Mock private ISecurityManagementService securityManagementService;

  @Nested
  class CanReadAdmin {
    @Test
    void canReadAdmin_technicalUser_roleAdmin() {
      User authenticated = new User();
      authenticated.setType(UserType.TECHNICAL);
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);
      Assertions.assertThat(userPermissionEvaluator.canReadAdmin(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canReadAdmin_technicalUser_noRoleAdmin() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);
      Assertions.assertThat(userPermissionEvaluator.canReadAdmin(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canReadAdmin_basicUser_sameUser_noPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_READ);
      Assertions.assertThat(userPermissionEvaluator.canReadAdmin(authenticated, authenticated))
          .isFalse();
    }

    @Test
    void canReadAdmin_basicUser_hasPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_READ);
      Assertions.assertThat(userPermissionEvaluator.canReadAdmin(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canReadAdmin_basicUser_noPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_READ);
      Assertions.assertThat(userPermissionEvaluator.canReadAdmin(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanWriteAdmin {
    @Test
    void canWriteAdmin_technicalUser_roleAdmin() {
      User authenticated = new User();
      authenticated.setType(UserType.TECHNICAL);
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);
      Assertions.assertThat(userPermissionEvaluator.canWriteAdmin(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canWriteAdmin_technicalUser_noRoleAdmin() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);
      Assertions.assertThat(userPermissionEvaluator.canWriteAdmin(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canWriteAdmin_basicUser_sameUser_noPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);
      Assertions.assertThat(userPermissionEvaluator.canWriteAdmin(authenticated, authenticated))
          .isFalse();
    }

    @Test
    void canWriteAdmin_basicUser_hasPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);
      Assertions.assertThat(userPermissionEvaluator.canWriteAdmin(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canWriteAdmin_basicUser_noPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);
      Assertions.assertThat(userPermissionEvaluator.canWriteAdmin(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanEnableAdmin {
    @Test
    void canEnableAdmin_technicalUser_alreadyEnabled() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);

      Assertions.assertThat(userPermissionEvaluator.canEnableAdmin(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canEnableAdmin_basicUser_alreadyEnabled() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);

      Assertions.assertThat(userPermissionEvaluator.canEnableAdmin(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canEnableAdmin_technicalUser_disabled_withPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      targetUser.setEnabled(false);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(userPermissionEvaluator.canEnableAdmin(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canEnableAdmin_technicalUser_disabled_withoutPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      targetUser.setEnabled(false);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(userPermissionEvaluator.canEnableAdmin(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canEnableAdmin_basicUser_disabled_withPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      targetUser.setEnabled(false);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(userPermissionEvaluator.canEnableAdmin(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canEnableAdmin_basicUser_disabled_withoutPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      targetUser.setEnabled(false);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(userPermissionEvaluator.canEnableAdmin(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanDisableAdmin {
    @Test
    void canDisableAdmin_technicalUser_alreadyDisabled() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      targetUser.setEnabled(false);

      Assertions.assertThat(userPermissionEvaluator.canDisableAdmin(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canDisableAdmin_basicUser_alreadyDisabled() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      targetUser.setEnabled(false);

      Assertions.assertThat(userPermissionEvaluator.canDisableAdmin(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canDisableAdmin_technicalUser_enabled_withPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(userPermissionEvaluator.canDisableAdmin(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canDisableAdmin_technicalUser_enabled_sameUser() {
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);

      Assertions.assertThat(userPermissionEvaluator.canDisableAdmin(targetUser, targetUser))
          .isFalse();
    }

    @Test
    void canDisableAdmin_technicalUser_enabled_withoutPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(userPermissionEvaluator.canDisableAdmin(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canDisableAdmin_basicUser_enabled_withPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(userPermissionEvaluator.canDisableAdmin(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canDisableAdmin_basicUser_enabled_sameUser() {
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);

      Assertions.assertThat(userPermissionEvaluator.canDisableAdmin(targetUser, targetUser))
          .isFalse();
    }

    @Test
    void canDisableAdmin_basicUser_enabled_withoutPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(userPermissionEvaluator.canDisableAdmin(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanResetPasswordAdmin {
    @Test
    void canResetPasswordAdmin_passwordAdminRecoveryDisabled() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);

      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordUserRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(
              userPermissionEvaluator.canResetPasswordAdmin(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canResetPasswordAdmin_passwordAdminRecoveryEnabled_technicalUser() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);

      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(
              userPermissionEvaluator.canResetPasswordAdmin(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canResetPasswordAdmin_passwordAdminRecoveryEnabled_technicalUser_noPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);

      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(
              userPermissionEvaluator.canResetPasswordAdmin(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canResetPasswordAdmin_passwordAdminRecoveryEnabled_basicUser() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);

      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(
              userPermissionEvaluator.canResetPasswordAdmin(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canResetPasswordAdmin_passwordAdminRecoveryEnabled_basicUser_noPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);

      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(
              userPermissionEvaluator.canResetPasswordAdmin(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanUpdatePasswordUser {
    @Test
    void canUpdatePasswordUser_sameUser() {
      User user = new User();
      Assertions.assertThat(userPermissionEvaluator.canUpdatePasswordUser(user, user)).isTrue();
    }

    @Test
    void canUpdatePasswordUser_differentUser() {
      User authenticated = new User();
      User targetUser = new User();
      Assertions.assertThat(
              userPermissionEvaluator.canUpdatePasswordUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canUpdatePasswordUser_noUserConnected() {
      User targetUser = new User();
      Assertions.assertThat(userPermissionEvaluator.canUpdatePasswordUser(null, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanOpenAnnouncementUser {
    @Test
    void canOpenAnnouncementUser_sameUser() {
      User user = new User();
      Assertions.assertThat(userPermissionEvaluator.canOpenAnnouncementUser(user, user)).isTrue();
    }

    @Test
    void canOpenAnnouncementUser_differentUser() {
      User authenticated = new User();
      User targetUser = new User();
      Assertions.assertThat(
              userPermissionEvaluator.canOpenAnnouncementUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canOpenAnnouncementUser_noUserConnected() {
      User targetUser = new User();
      Assertions.assertThat(userPermissionEvaluator.canOpenAnnouncementUser(null, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanCloseAnnouncementUser {
    @Test
    void canCloseAnnouncementUser_sameUser() {
      User user = new User();
      Assertions.assertThat(userPermissionEvaluator.canCloseAnnouncementUser(user, user)).isTrue();
    }

    @Test
    void canCloseAnnouncementUser_differentUser() {
      User authenticated = new User();
      User targetUser = new User();
      Assertions.assertThat(
              userPermissionEvaluator.canCloseAnnouncementUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canCloseAnnouncementUser_noUserConnected() {
      User targetUser = new User();
      Assertions.assertThat(userPermissionEvaluator.canCloseAnnouncementUser(null, targetUser))
          .isFalse();
    }
  }
}
