package test.core.business.announcement;

import basicapp.back.business.announcement.dao.IAnnouncementDao;
import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.model.atomic.AnnouncementType;
import basicapp.back.business.announcement.service.business.AnnouncementServiceImpl;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.business.IUserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.json.JSONArray;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestAnnouncementServiceMock {

  @InjectMocks @Spy private AnnouncementServiceImpl announcementService;
  @Mock private IUserService userService;
  @Mock private IAnnouncementDao announcementDao;

  @Test
  void gestg() {
    List<String> test = new ArrayList<>();
    test.add("aa");
    test.add("bb");
    test.add("cc");
    JSONArray array = new JSONArray(test);
    array.put(test);
    Assertions.assertThat(array).isNotNull();
  }

  @Nested
  class TestSaveAnnouncement {
    @Test
    void testSaveAnnouncement_announcementNull_throwNullPointerException() {
      Assertions.assertThatThrownBy(() -> announcementService.saveAnnouncement(null))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testSaveAnnouncement_typeNull_throwNullPointerException() {
      Assertions.assertThatThrownBy(() -> announcementService.saveAnnouncement(new Announcement()))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testSaveAnnouncement_new_callCreate() throws SecurityServiceException, ServiceException {
      Announcement announcement = new Announcement();
      announcement.setType(AnnouncementType.SERVICE_INTERRUPTION);
      Mockito.doNothing().when(announcementService).cleanAnnouncement(announcement);
      Mockito.doNothing().when(announcementService).create(announcement);

      Assertions.assertThatCode(() -> announcementService.saveAnnouncement(announcement))
          .doesNotThrowAnyException();

      Mockito.verify(announcementService).cleanAnnouncement(announcement);
      Mockito.verify(announcementService).create(announcement);
    }

    @Test
    void testSaveAnnouncement_notNew_callUpdate()
        throws SecurityServiceException, ServiceException {
      Announcement announcement = new Announcement();
      announcement.setType(AnnouncementType.SERVICE_INTERRUPTION);
      announcement.setId(-1L);
      Mockito.doNothing().when(announcementService).cleanAnnouncement(announcement);
      Mockito.doNothing().when(announcementService).update(announcement);

      Assertions.assertThatCode(() -> announcementService.saveAnnouncement(announcement))
          .doesNotThrowAnyException();

      Mockito.verify(announcementService).cleanAnnouncement(announcement);
      Mockito.verify(announcementService).update(announcement);
    }
  }

  @Nested
  class TestCleanAnnouncement {
    @Test
    void cleanWithoutSaving_announcementNull_doNothing() {
      Assertions.assertThatCode(() -> announcementService.cleanWithoutSaving(null))
          .doesNotThrowAnyException();
      Mockito.verify(announcementService, Mockito.never())
          .cleanAnnouncement(Mockito.any(Announcement.class));
    }

    @Test
    void cleanWithoutSaving_typeNull_doNothing() {
      Assertions.assertThatCode(() -> announcementService.cleanWithoutSaving(new Announcement()))
          .doesNotThrowAnyException();
      Mockito.verify(announcementService, Mockito.never())
          .cleanAnnouncement(Mockito.any(Announcement.class));
    }

    @Test
    void cleanWithoutSaving_withType_callCleanAnnoucement() {
      Announcement announcement = new Announcement();
      announcement.setType(AnnouncementType.SERVICE_INTERRUPTION);
      Assertions.assertThatCode(() -> announcementService.cleanWithoutSaving(announcement))
          .doesNotThrowAnyException();
      Mockito.verify(announcementService).cleanAnnouncement(announcement);
    }

    @Test
    void cleanAnnouncement_announcementNull_throwNullPointerException() {
      Assertions.assertThatThrownBy(() -> announcementService.cleanAnnouncement(null))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    void cleanAnnouncement_typeNull_throwNullPointerException() {
      Assertions.assertThatThrownBy(() -> announcementService.cleanAnnouncement(new Announcement()))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    void cleanAnnouncement_typeServiceInterruption_cleanTitleAndDescription() {
      Announcement announcement = new Announcement();
      announcement.setType(AnnouncementType.SERVICE_INTERRUPTION);
      announcement.getTitle().setFr("title");
      announcement.getDescription().setFr("description");
      announcement.getInterruption().setStartDateTime(LocalDateTime.of(2024, 1, 1, 10, 0));
      announcement.getInterruption().setEndDateTime(LocalDateTime.of(2024, 1, 2, 10, 0));
      announcementService.cleanAnnouncement(announcement);

      Assertions.assertThat(announcement.getTitle().getFr()).isNull();
      Assertions.assertThat(announcement.getDescription().getFr()).isNull();
      Assertions.assertThat(announcement.getInterruption().getStartDateTime())
          .isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
      Assertions.assertThat(announcement.getInterruption().getEndDateTime())
          .isEqualTo(LocalDateTime.of(2024, 1, 2, 10, 0));
    }

    @Test
    void cleanAnnouncement_typeOther_cleanDates() {
      Announcement announcement = new Announcement();
      announcement.setType(AnnouncementType.OTHER);
      announcement.getTitle().setFr("title");
      announcement.getDescription().setFr("description");
      announcement.getInterruption().setStartDateTime(LocalDateTime.of(2024, 1, 1, 10, 0));
      announcement.getInterruption().setEndDateTime(LocalDateTime.of(2024, 1, 2, 10, 0));
      announcementService.cleanAnnouncement(announcement);

      Assertions.assertThat(announcement.getTitle().getFr()).isEqualTo("title");
      Assertions.assertThat(announcement.getDescription().getFr()).isEqualTo("description");
      Assertions.assertThat(announcement.getInterruption().getStartDateTime()).isNull();
      Assertions.assertThat(announcement.getInterruption().getEndDateTime()).isNull();
    }
  }

  @Nested
  class TestIsOpen {
    @Test
    void testIsOpen_userNull_returnFalse() {
      Mockito.when(userService.getAuthenticatedUser()).thenReturn(null);
      Assertions.assertThat(announcementService.isOpen()).isFalse();
    }

    @Test
    void testIsOpen_lastActionDateNull_returnAlwaysTrue() {
      User user = new User();
      user.getAnnouncementInformation().setOpen(false);
      Mockito.when(userService.getAuthenticatedUser()).thenReturn(user);
      Assertions.assertThat(announcementService.isOpen()).isTrue();
    }

    @Test
    void testIsOpen_moreRecentAnnouncementFound_returnAlwaysTrue() {
      User user = new User();
      user.getAnnouncementInformation().setOpen(false);
      user.getAnnouncementInformation()
          .setLastActionDate(
              LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
      Mockito.when(userService.getAuthenticatedUser()).thenReturn(user);
      Mockito.when(announcementDao.getMostRecentPublicationStartDate())
          .thenReturn(LocalDateTime.of(2024, 2, 1, 10, 0));
      Assertions.assertThat(announcementService.isOpen()).isTrue();
    }

    @Test
    void testIsOpen_userClosePopup_returnFalse() {
      User user = new User();
      user.getAnnouncementInformation().setOpen(false);
      user.getAnnouncementInformation()
          .setLastActionDate(
              LocalDate.of(2024, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
      Mockito.when(userService.getAuthenticatedUser()).thenReturn(user);
      Mockito.when(announcementDao.getMostRecentPublicationStartDate())
          .thenReturn(LocalDateTime.of(2024, 1, 1, 10, 0));
      Assertions.assertThat(announcementService.isOpen()).isFalse();
    }

    @Test
    void testIsOpen_userOpenPopUp_returnTrue() {
      User user = new User();
      user.getAnnouncementInformation().setOpen(true);
      user.getAnnouncementInformation()
          .setLastActionDate(
              LocalDate.of(2024, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
      Mockito.when(userService.getAuthenticatedUser()).thenReturn(user);
      Mockito.when(announcementDao.getMostRecentPublicationStartDate())
          .thenReturn(LocalDateTime.of(2024, 1, 1, 10, 0));
      Assertions.assertThat(announcementService.isOpen()).isTrue();
    }
  }
}
