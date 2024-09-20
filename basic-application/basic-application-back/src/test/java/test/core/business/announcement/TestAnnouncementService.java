package test.core.business.announcement;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ANNOUNCEMENT_WRITE;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.model.atomic.AnnouncementType;
import basicapp.back.business.announcement.service.controller.IAnnouncementControllerService;
import basicapp.back.business.user.model.atomic.UserType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
public class TestAnnouncementService extends AbstractBasicApplicationTestCase {

  private static final String BASIC_USERNAME_WITH_PERMISSIONS = "BASIC_USERNAME_WITH_PERMISSIONS";
  private static final String ADMIN_USERNAME = "ADMIN_USERNAME";
  private static final String BASIC_USERNAME_WITHOUT_PERMISSIONS =
      "BASIC_USERNAME_WITHOUT_PERMISSIONS";

  @Autowired private IAnnouncementControllerService announcementControllerService;

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
        GLOBAL_ANNOUNCEMENT_WRITE);
  }

  @WithUserDetails(
      value = BASIC_USERNAME_WITH_PERMISSIONS,
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  @Nested
  class SaveAnnouncement {
    @Test
    void saveAnnouncement_new() throws SecurityServiceException, ServiceException {
      Announcement announcement =
          entityDatabaseHelper.createAnnouncement(
              a -> {
                a.getTitle().setFr("testTitre");
                a.getDescription().setFr("testDescription");
              },
              false);

      announcementControllerService.saveAnnouncement(announcement);
      entityManagerReset();
      Announcement announcementBdd = announcementService.getById(announcement.getId());
      Assertions.assertThat(announcementBdd).isNotNull();
      Assertions.assertThat(announcementBdd.getType())
          .isEqualTo(AnnouncementType.SERVICE_INTERRUPTION);
      Assertions.assertThat(announcementBdd.getTitle().getFr()).isNull();
      Assertions.assertThat(announcementBdd.getDescription().getFr()).isNull();
      Assertions.assertThat(announcementBdd.getInterruption().getStartDateTime())
          .isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
      Assertions.assertThat(announcementBdd.getInterruption().getEndDateTime())
          .isEqualTo(LocalDateTime.of(2024, 1, 2, 10, 0));
      Assertions.assertThat(announcementBdd.getPublication().getStartDateTime())
          .isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
      Assertions.assertThat(announcementBdd.getPublication().getEndDateTime())
          .isEqualTo(LocalDateTime.of(2024, 1, 2, 10, 0));

      Assertions.assertThat(new Date()).isInSameDayAs(announcementBdd.getCreation().getDate());
      Assertions.assertThat(new Date()).isInSameDayAs(announcementBdd.getModification().getDate());
    }

    @Test
    void saveNewAnnouncement_update() throws SecurityServiceException, ServiceException {
      Announcement announcement =
          entityDatabaseHelper.createAnnouncement(
              a -> {
                a.getTitle().setFr("testTitre");
                a.getDescription().setFr("testDescription");
              },
              true);
      announcement
          .getCreation()
          .setDate(LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
      announcement
          .getModification()
          .setDate(LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
      announcementService.flush();

      entityManagerReset();
      Announcement announcementToUpdate = announcementService.getById(announcement.getId());
      announcementToUpdate.setType(AnnouncementType.OTHER);
      announcementService.saveAnnouncement(announcementService.getById(announcement.getId()));
      entityManagerReset();

      Announcement announcementBdd = announcementService.getById(announcement.getId());
      Assertions.assertThat(announcementBdd).isNotNull();
      Assertions.assertThat(announcementBdd.getType()).isEqualTo(AnnouncementType.OTHER);
      Assertions.assertThat(announcementBdd.getTitle().getFr()).isEqualTo("testTitre");
      Assertions.assertThat(announcementBdd.getDescription().getFr()).isEqualTo("testDescription");
      Assertions.assertThat(announcementBdd.getInterruption().getStartDateTime()).isNull();
      Assertions.assertThat(announcementBdd.getInterruption().getEndDateTime()).isNull();
      Assertions.assertThat(announcementBdd.getPublication().getStartDateTime())
          .isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
      Assertions.assertThat(announcementBdd.getPublication().getEndDateTime())
          .isEqualTo(LocalDateTime.of(2024, 1, 2, 10, 0));

      Assertions.assertThat(DateUtil.parse("2024-01-01"))
          .isInSameDayAs(announcementBdd.getCreation().getDate());
      Assertions.assertThat(new Date()).isInSameDayAs(announcementBdd.getModification().getDate());
    }

    @WithUserDetails(value = ADMIN_USERNAME, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testSaveAnnouncement_TechnicalUserAuthenticate_doesNotThrowException() {
      Assertions.assertThatCode(
              () ->
                  announcementControllerService.saveAnnouncement(
                      entityDatabaseHelper.createAnnouncement(null, false)))
          .doesNotThrowAnyException();
    }

    @WithUserDetails(
        value = BASIC_USERNAME_WITHOUT_PERMISSIONS,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void
        testSaveAnnouncement_UserWithoutPermissionsAuthenticate_throwAuthorizationDeniedException() {
      Assertions.assertThatThrownBy(
              () ->
                  announcementControllerService.saveAnnouncement(
                      entityDatabaseHelper.createAnnouncement(null, false)))
          .isInstanceOf(AuthorizationDeniedException.class);
    }
  }

  @WithUserDetails(
      value = BASIC_USERNAME_WITH_PERMISSIONS,
      setupBefore = TestExecutionEvent.TEST_EXECUTION)
  @Test
  void cleanWithoutSaving() throws SecurityServiceException, ServiceException {
    Announcement announcement = entityDatabaseHelper.createAnnouncement(null, true);
    announcement.setType(AnnouncementType.OTHER);
    announcementControllerService.cleanWithoutSaving(announcement);
    entityManagerReset();
    Announcement announcementBdd = announcementService.getById(announcement.getId());
    Assertions.assertThat(announcementBdd.getType())
        .isEqualTo(AnnouncementType.SERVICE_INTERRUPTION);
  }

  @Test
  void listEnable() throws SecurityServiceException, ServiceException {
    // not in list
    // start after today
    entityDatabaseHelper.createAnnouncement(
        a -> {
          a.getPublication().setStartDateTime(LocalDateTime.now().plusDays(1));
          a.getPublication().setEndDateTime(LocalDateTime.now().plusDays(3));
        },
        true);

    // finish later
    entityDatabaseHelper.createAnnouncement(
        a -> {
          a.getPublication().setStartDateTime(LocalDateTime.now().minusDays(3));
          a.getPublication().setEndDateTime(LocalDateTime.now().minusDays(1));
        },
        true);

    // finish now
    entityDatabaseHelper.createAnnouncement(
        a -> {
          a.getPublication().setStartDateTime(LocalDateTime.now().minusDays(3));
          a.getPublication().setEndDateTime(LocalDateTime.now());
        },
        true);

    // disable
    entityDatabaseHelper.createAnnouncement(
        a -> {
          a.getPublication().setStartDateTime(LocalDateTime.now().minusDays(3));
          a.getPublication().setEndDateTime(LocalDateTime.now().plusDays(3));
          a.setEnabled(false);
        },
        true);

    // in list
    // enable
    Announcement announcementEnable =
        entityDatabaseHelper.createAnnouncement(
            a -> {
              a.getPublication().setStartDateTime(LocalDateTime.now().minusDays(3));
              a.getPublication().setEndDateTime(LocalDateTime.now().plusDays(3));
            },
            true);

    // start today
    Announcement announcementStartToday =
        entityDatabaseHelper.createAnnouncement(
            a -> {
              a.getPublication().setStartDateTime(LocalDateTime.now());
              a.getPublication().setEndDateTime(LocalDateTime.now().plusDays(3));
            },
            true);

    // finish today + 1 min
    Announcement announcementFinishToday_plusDays1min =
        entityDatabaseHelper.createAnnouncement(
            a -> {
              a.getPublication().setStartDateTime(LocalDateTime.now().minusDays(3));
              a.getPublication().setEndDateTime(LocalDateTime.now().plusMinutes(1));
            },
            true);

    entityManagerReset();
    Assertions.assertThat(announcementControllerService.listEnabled())
        .containsExactlyInAnyOrder(
            announcementEnable, announcementStartToday, announcementFinishToday_plusDays1min);
  }

  @Nested
  class TestDeleteAnnoucement {
    @WithUserDetails(
        value = BASIC_USERNAME_WITH_PERMISSIONS,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testDeleteAnnoucement() throws SecurityServiceException, ServiceException {
      Announcement announcement = entityDatabaseHelper.createAnnouncement(null, true);
      entityManagerReset();
      Assertions.assertThat(announcementService.list()).size().isEqualTo(1);
      announcementControllerService.deleteAnnouncement(
          announcementService.getById(announcement.getId()));
      entityManagerReset();
      Assertions.assertThat(announcementService.list()).isEmpty();
    }

    @WithUserDetails(value = ADMIN_USERNAME, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void TestDeleteAnnoucement_TechnicalUserAuthenticate_doesNotThrowException() {
      Assertions.assertThatCode(
              () ->
                  announcementControllerService.deleteAnnouncement(
                      entityDatabaseHelper.createAnnouncement(null, true)))
          .doesNotThrowAnyException();
    }

    @WithUserDetails(
        value = BASIC_USERNAME_WITHOUT_PERMISSIONS,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void
        TestDeleteAnnoucement_UserWithoutPermissionsAuthenticate_throwAuthorizationDeniedException() {
      Assertions.assertThatThrownBy(
              () ->
                  announcementControllerService.deleteAnnouncement(
                      entityDatabaseHelper.createAnnouncement(null, true)))
          .isInstanceOf(AuthorizationDeniedException.class);
    }
  }
}
