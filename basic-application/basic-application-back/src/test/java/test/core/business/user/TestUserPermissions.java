package test.core.business.user;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_WRITE;
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
  class CanOpenAnnouncement {
    @Test
    void canOpenAnnouncement_announcementClose_sameUser() {
      User user = new User();
      user.getAnnouncementInformation().setOpen(false);

      Assertions.assertThat(userPermissionEvaluator.canOpenAnnouncement(user, user)).isTrue();
    }

    @Test
    void canOpenAnnouncement_announcementOpen_sameUser() {
      User user = new User();
      user.getAnnouncementInformation().setOpen(true);
      Assertions.assertThat(userPermissionEvaluator.canOpenAnnouncement(user, user)).isFalse();
    }

    @Test
    void canOpenAnnouncement_differentUser() {
      User authenticated = new User();
      User targetUser = new User();
      Assertions.assertThat(userPermissionEvaluator.canOpenAnnouncement(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanCloseAnnouncement {
    @Test
    void canCloseAnnouncement_announcementOpen_sameUser() {
      User user = new User();
      user.getAnnouncementInformation().setOpen(true);
      Assertions.assertThat(userPermissionEvaluator.canCloseAnnouncement(user, user)).isTrue();
    }

    @Test
    void canCloseAnnouncement_announcementClose_sameUser() {
      User user = new User();
      user.getAnnouncementInformation().setOpen(false);
      Assertions.assertThat(userPermissionEvaluator.canCloseAnnouncement(user, user)).isFalse();
    }

    @Test
    void canCloseAnnouncement_differentUser() {
      User authenticated = new User();
      User targetUser = new User();
      Assertions.assertThat(userPermissionEvaluator.canCloseAnnouncement(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanReadUser {
    @Test
    void canReadUser_technicalUser_userAdmin() {
      User authenticated = new User();
      authenticated.setType(UserType.TECHNICAL);
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);
      Assertions.assertThat(userPermissionEvaluator.canReadUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canReadUser_technicalUser_userBasic() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);
      Assertions.assertThat(userPermissionEvaluator.canReadUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canReadUser_basicUser_sameUser() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      Assertions.assertThat(userPermissionEvaluator.canReadUser(authenticated, authenticated))
          .isTrue();
    }

    @Test
    void canReadUser_basicUser_hasPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_READ);
      Assertions.assertThat(userPermissionEvaluator.canReadUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canReadUser_basicUser_NoPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_READ);
      Assertions.assertThat(userPermissionEvaluator.canReadUser(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanWriteUser {
    @Test
    void canReadUser_technicalUser_userAdmin() {
      User authenticated = new User();
      authenticated.setType(UserType.TECHNICAL);
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);
      Assertions.assertThat(userPermissionEvaluator.canWriteUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canReadUser_technicalUser_userBasic() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);
      Assertions.assertThat(userPermissionEvaluator.canWriteUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canReadUser_basicUser_sameUser() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      Assertions.assertThat(userPermissionEvaluator.canWriteUser(authenticated, authenticated))
          .isTrue();
    }

    @Test
    void canReadUser_basicUser_hasPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);
      Assertions.assertThat(userPermissionEvaluator.canWriteUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canReadUser_basicUser_NoPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);
      Assertions.assertThat(userPermissionEvaluator.canWriteUser(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanAffecterRole {
    @Test
    void canAffecterRole_hasPermission() {
      User authenticated = new User();
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_WRITE);
      Assertions.assertThat(userPermissionEvaluator.canAffectRoles(authenticated)).isTrue();
    }

    @Test
    void canAffecterRole_NoPermission() {
      User authenticated = new User();
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_WRITE);
      Assertions.assertThat(userPermissionEvaluator.canAffectRoles(authenticated)).isFalse();
    }
  }

  @Nested
  class CanEnableUser {
    @Test
    void CanEnableUser_TechnicalUser_userEnable() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);

      Assertions.assertThat(userPermissionEvaluator.canEnableUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void CanEnableUser_BasicUser_userEnable() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);

      Assertions.assertThat(userPermissionEvaluator.canEnableUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void CanEnableUser_TechnicalUser_userDisbale_withPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      targetUser.setEnabled(false);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(userPermissionEvaluator.canEnableUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void CanEnableUser_TechnicalUser_userDisable_withoutPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      targetUser.setEnabled(false);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(userPermissionEvaluator.canEnableUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void CanEnableUser_BasicUser_userDisable_withPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      targetUser.setEnabled(false);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(userPermissionEvaluator.canEnableUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void CanEnableUser_BasicUser_userDisable_withoutPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      targetUser.setEnabled(false);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(userPermissionEvaluator.canEnableUser(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanDisableUser {
    @Test
    void canDisableUser_TechnicalUser_userDisable() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      targetUser.setEnabled(false);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canDisableUser_BasicUser_userDisable() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      targetUser.setEnabled(false);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canDisableUser_TechnicalUser_userActif_withPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canDisableUser_TechnicalUser_userActif_sameUser() {
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(targetUser, targetUser))
          .isFalse();
    }

    @Test
    void canDisableUser_TechnicalUser_userActif_withoutPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canDisableUser_BasicUser_userActif_withPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canDisableUser_BasicUser_userActif_withPermission_sameUser() {
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(targetUser, targetUser))
          .isFalse();
    }

    @Test
    void canDisableUser_BasicUser_userActif_withoutPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanAdminRecoveryPassword {
    @Test
    void CanAdminRecoveryPassword_PasswordAdminRecoveryDisable() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);

      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordUserRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);

      Assertions.assertThat(
              userPermissionEvaluator.canAdminRecoveryPassword(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void CanAdminRecoveryPassword_PasswordAdminRecoveryEnabled_TechnicalUser() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);

      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(
              userPermissionEvaluator.canAdminRecoveryPassword(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void CanAdminRecoveryPassword_PasswordAdminRecoveryEnabled_TechnicalUser_noPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);

      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(
              userPermissionEvaluator.canAdminRecoveryPassword(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void CanAdminRecoveryPassword_PasswordAdminRecoveryEnabled_BasicUser() {
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
              userPermissionEvaluator.canAdminRecoveryPassword(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void CanAdminRecoveryPassword_PasswordAdminRecoveryEnabled_BasicUser_noPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.BASIC);

      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(
              userPermissionEvaluator.canAdminRecoveryPassword(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanUserRecoveryPassword {

    @Test
    void CanUserRecoveryPassword_noUserConnected_PasswordUserRecoveryEnabled() {
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordUserRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Assertions.assertThat(userPermissionEvaluator.canUserRecoveryPassword(null, targetUser))
          .isTrue();
    }

    @Test
    void CanUserRecoveryPassword_noUserConnected_PasswordUserRecoveryDisable() {
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminUpdate);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Assertions.assertThat(userPermissionEvaluator.canUserRecoveryPassword(null, targetUser))
          .isFalse();
    }

    @Test
    void CanUserRecoveryPassword_UserConnected_canWrite_PasswordUserRecoveryEnabled() {
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      User authenticated = new User();
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordUserRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true).when(userPermissionEvaluator).canWriteUser(authenticated, targetUser);
      Assertions.assertThat(
              userPermissionEvaluator.canUserRecoveryPassword(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void CanUserRecoveryPassword_UserConnected_canWrite_PasswordUserRecoveryDisable() {
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      User authenticated = new User();
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true).when(userPermissionEvaluator).canWriteUser(authenticated, targetUser);
      Assertions.assertThat(
              userPermissionEvaluator.canUserRecoveryPassword(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void CanUserRecoveryPassword_UserConnected_cannot_Write() {
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      User authenticated = new User();

      Mockito.doReturn(false).when(userPermissionEvaluator).canWriteUser(authenticated, targetUser);
      Assertions.assertThat(
              userPermissionEvaluator.canUserRecoveryPassword(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanUserEditPassword {

    @Test
    void CanUserRecoveryPassword_noUserConnected_PasswordUserRecoveryEnabled() {
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordUserUpdate);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Assertions.assertThat(userPermissionEvaluator.canUserEditPassword(null, targetUser)).isTrue();
    }

    @Test
    void CanUserRecoveryPassword_noUserConnected_PasswordUserRecoveryDisable() {
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminUpdate);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Assertions.assertThat(userPermissionEvaluator.canUserEditPassword(null, targetUser))
          .isFalse();
    }

    @Test
    void CanUserRecoveryPassword_UserConnected_canWrite_PasswordUserRecoveryEnabled() {
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      User authenticated = new User();
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordUserUpdate);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true).when(userPermissionEvaluator).canWriteUser(authenticated, targetUser);
      Assertions.assertThat(userPermissionEvaluator.canUserEditPassword(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void CanUserRecoveryPassword_UserConnected_canWrite_PasswordUserRecoveryDisable() {
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      User authenticated = new User();
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true).when(userPermissionEvaluator).canWriteUser(authenticated, targetUser);
      Assertions.assertThat(userPermissionEvaluator.canUserEditPassword(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void CanUserRecoveryPassword_UserConnected_cannot_Write() {
      User targetUser = new User();
      targetUser.setType(UserType.TECHNICAL);
      User authenticated = new User();

      Mockito.doReturn(false).when(userPermissionEvaluator).canWriteUser(authenticated, targetUser);
      Assertions.assertThat(userPermissionEvaluator.canUserEditPassword(authenticated, targetUser))
          .isFalse();
    }
  }
}
