package test.core.business.referencedata;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_WRITE;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.security.service.permission.ReferenceDataPermissionEvaluatorImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestReferenceDataPermissions {

  @Spy @InjectMocks private ReferenceDataPermissionEvaluatorImpl referenceDataPermissionEvaluator;

  @Nested
  class CanReadReferenceData {
    @Test
    void canReadReferenceData_WithPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(true)
          .when(referenceDataPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_REFERENCE_DATA_READ);
      Assertions.assertThat(referenceDataPermissionEvaluator.canRead(authenticated)).isTrue();
    }

    @Test
    void canWriteReferenceData_NoPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(false)
          .when(referenceDataPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_REFERENCE_DATA_READ);
      Assertions.assertThat(referenceDataPermissionEvaluator.canRead(authenticated)).isFalse();
    }
  }

  @Nested
  class CanWriteCreateReferenceData {

    @Test
    void canWriteReferenceData_UpdateReferenceData_NoEditable_returnFalse() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      ReferenceData<?> referenceData = new ReferenceData<>();
      referenceData.setId(-1L);
      referenceData.setEditable(false);

      Assertions.assertThat(referenceDataPermissionEvaluator.canWrite(authenticated, referenceData))
          .isFalse();
    }

    @Test
    void canWriteReferenceData_UpdateReferenceData_Editable_withPermissions_returnTrue() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      ReferenceData<?> referenceData = new ReferenceData<>();
      referenceData.setId(-1L);
      referenceData.setEditable(true);

      Mockito.doReturn(true)
          .when(referenceDataPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_REFERENCE_DATA_READ);

      Mockito.doReturn(true)
          .when(referenceDataPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_REFERENCE_DATA_WRITE);

      Assertions.assertThat(referenceDataPermissionEvaluator.canWrite(authenticated, referenceData))
          .isTrue();
    }

    @Test
    void canWriteReferenceData_CreateReferenceData_NoEditable_withPermissions_returnTrue() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      ReferenceData<?> referenceData = new ReferenceData<>();
      referenceData.setEditable(false);

      Mockito.doReturn(true)
          .when(referenceDataPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_REFERENCE_DATA_READ);

      Mockito.doReturn(true)
          .when(referenceDataPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_REFERENCE_DATA_WRITE);

      Assertions.assertThat(referenceDataPermissionEvaluator.canWrite(authenticated, referenceData))
          .isTrue();
    }

    @Test
    void canWriteReferenceData_UpdateReferenceData_Editable_NoPermission_returnFalse() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      ReferenceData<?> referenceData = new ReferenceData<>();
      referenceData.setId(-1L);
      referenceData.setEditable(true);

      Mockito.doReturn(false)
          .when(referenceDataPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_REFERENCE_DATA_READ);

      Mockito.lenient()
          .doReturn(false)
          .when(referenceDataPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_REFERENCE_DATA_WRITE);

      Assertions.assertThat(referenceDataPermissionEvaluator.canWrite(authenticated, referenceData))
          .isFalse();
    }

    @Test
    void canWriteReferenceData_UpdateReferenceData_Editable_OnlyRead_returnFalse() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      ReferenceData<?> referenceData = new ReferenceData<>();
      referenceData.setId(-1L);
      referenceData.setEditable(true);

      Mockito.doReturn(true)
          .when(referenceDataPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_REFERENCE_DATA_READ);

      Mockito.doReturn(false)
          .when(referenceDataPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_REFERENCE_DATA_WRITE);

      Assertions.assertThat(referenceDataPermissionEvaluator.canWrite(authenticated, referenceData))
          .isFalse();
    }

    @Test
    void canWriteReferenceData_UpdateReferenceData_Editable_OnlyWrite_returnFalse() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      ReferenceData<?> referenceData = new ReferenceData<>();
      referenceData.setId(-1L);
      referenceData.setEditable(true);

      Mockito.doReturn(false)
          .when(referenceDataPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_REFERENCE_DATA_READ);

      Mockito.lenient()
          .doReturn(true)
          .when(referenceDataPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_REFERENCE_DATA_WRITE);

      Assertions.assertThat(referenceDataPermissionEvaluator.canWrite(authenticated, referenceData))
          .isFalse();
    }
  }
}
