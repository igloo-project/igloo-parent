package basicapp.back.business.announcement.service.business;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.repository.IAnnouncementRepository;
import basicapp.back.business.history.service.IHistoryEventSummaryService;
import basicapp.back.business.user.service.business.IUserService;
import com.google.common.annotations.VisibleForTesting;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnnouncementServiceImpl implements IAnnouncementService {

  private final IAnnouncementRepository announcementRepository;
  private final IHistoryEventSummaryService historyEventSummaryService;
  private final IUserService userService;

  @Autowired
  public AnnouncementServiceImpl(
      IAnnouncementRepository announcementRepository,
      IHistoryEventSummaryService historyEventSummaryService,
      IUserService userService) {
    this.announcementRepository = announcementRepository;
    this.historyEventSummaryService = historyEventSummaryService;
    this.userService = userService;
  }

  @Override
  @Transactional
  public void saveAnnouncement(Announcement announcement) {
    Objects.requireNonNull(announcement);
    Objects.requireNonNull(announcement.getType());
    cleanAnnouncement(announcement);
    if (announcement.isNew()) {
      historyEventSummaryService.refresh(announcement.getCreation());
    }
    historyEventSummaryService.refresh(announcement.getModification());
    announcementRepository.save(announcement);
  }

  @Override
  @Transactional
  public void deleteAnnouncement(Announcement announcement) {
    Objects.requireNonNull(announcement);
    announcementRepository.delete(announcement);
  }

  @Override
  @Transactional(readOnly = true)
  public void cleanWithoutSaving(Announcement announcement) {
    Optional.ofNullable(announcement)
        .filter(a -> a.getType() != null)
        .ifPresent(this::cleanAnnouncement);
  }

  @VisibleForTesting
  public void cleanAnnouncement(Announcement announcement) {
    Objects.requireNonNull(announcement);
    Objects.requireNonNull(announcement.getType());
    switch (announcement.getType()) {
      case SERVICE_INTERRUPTION -> {
        announcement.setTitle(null);
        announcement.setDescription(null);
      }
      case OTHER -> announcement.setInterruption(null);
      default -> throw new IllegalSwitchValueException(announcement.getType());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Announcement> listEnabled() {
    return announcementRepository.findAllEnabled();
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isOpen() {
    return Optional.ofNullable(userService.getAuthenticatedUser())
        .map(
            user -> {
              Instant lastActionDate = user.getAnnouncementInformation().getLastActionDate();
              LocalDateTime mostRecentPublicationStartDate =
                  announcementRepository.getMostRecentPublicationStartDate();

              if (lastActionDate == null
                  || LocalDateTime.ofInstant(lastActionDate, ZoneId.systemDefault())
                      .isBefore(mostRecentPublicationStartDate)) {
                return true;
              }
              return user.getAnnouncementInformation().isOpen();
            })
        .orElse(false);
  }
}
