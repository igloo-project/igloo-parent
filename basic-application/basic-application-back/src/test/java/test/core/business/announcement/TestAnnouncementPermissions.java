package test.core.business.announcement;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ANNOUNCEMENT_WRITE;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.security.service.permission.AnnouncementPermissionEvaluatorImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestAnnouncementPermissions {

  @Spy @InjectMocks private AnnouncementPermissionEvaluatorImpl announcementPermissionEvaluator;

  @Nested
  class CanWriteAnnouncement {
    @Test
    void canWriteAnnouncement_withPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(true)
          .when(announcementPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ANNOUNCEMENT_WRITE);
      Assertions.assertThat(announcementPermissionEvaluator.canWriteAnnouncement(authenticated))
          .isTrue();
    }

    @Test
    void canWriteAnnouncement_noPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(false)
          .when(announcementPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ANNOUNCEMENT_WRITE);
      Assertions.assertThat(announcementPermissionEvaluator.canWriteAnnouncement(authenticated))
          .isFalse();
    }
  }

  @Nested
  class CanRemoveAnnouncement {
    @Test
    void canRemoveAnnouncement_withPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(true)
          .when(announcementPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ANNOUNCEMENT_WRITE);
      Assertions.assertThat(announcementPermissionEvaluator.canRemoveAnnouncement(authenticated))
          .isTrue();
    }

    @Test
    void canRemoveAnnouncement_noPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(false)
          .when(announcementPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ANNOUNCEMENT_WRITE);
      Assertions.assertThat(announcementPermissionEvaluator.canRemoveAnnouncement(authenticated))
          .isFalse();
    }
  }
}
