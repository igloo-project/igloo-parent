package test.core.business.role;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_WRITE;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.security.service.permission.RolePermissionEvaluatorImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestRolePermissions {

  @Spy @InjectMocks private RolePermissionEvaluatorImpl rolePermissionEvaluator;

  @Nested
  class CanReadRole {
    @Test
    void canReadRole_WithPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(true)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_READ);
      Assertions.assertThat(rolePermissionEvaluator.canRead(authenticated)).isTrue();
    }

    @Test
    void canWriteRole_NoPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(false)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_READ);
      Assertions.assertThat(rolePermissionEvaluator.canRead(authenticated)).isFalse();
    }
  }

  @Nested
  class CanWriteRole {
    @Test
    void canWriteRole_WithPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(true)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_READ);

      Mockito.doReturn(true)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_WRITE);

      Assertions.assertThat(rolePermissionEvaluator.canWrite(authenticated)).isTrue();
    }

    @Test
    void canWriteRole_NoPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(false)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_READ);

      Mockito.lenient()
          .doReturn(false)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_WRITE);

      Assertions.assertThat(rolePermissionEvaluator.canWrite(authenticated)).isFalse();
    }

    @Test
    void canWriteRole_OnlyRead() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(true)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_READ);

      Mockito.doReturn(false)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_WRITE);

      Assertions.assertThat(rolePermissionEvaluator.canWrite(authenticated)).isFalse();
    }

    @Test
    void canWriteRole_OnlyWrite() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(false)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_READ);

      Mockito.lenient()
          .doReturn(true)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_WRITE);

      Assertions.assertThat(rolePermissionEvaluator.canWrite(authenticated)).isFalse();
    }
  }
}
