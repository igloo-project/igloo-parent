package basicapp.back.business.announcement.service.business;

import basicapp.back.business.announcement.dao.IAnnouncementDao;
import basicapp.back.business.announcement.model.Announcement;
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
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnnouncementServiceImpl extends GenericEntityServiceImpl<Long, Announcement>
    implements IAnnouncementService {

  private final IAnnouncementDao dao;
  private final IHistoryEventSummaryService historyEventSummaryService;
  private final IUserService userService;

  @Autowired
  public AnnouncementServiceImpl(
      IAnnouncementDao dao,
      IHistoryEventSummaryService historyEventSummaryService,
      IUserService userService) {
    super(dao);
    this.dao = dao;
    this.historyEventSummaryService = historyEventSummaryService;
    this.userService = userService;
  }

  @Override
  protected void createEntity(Announcement entity)
      throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getCreation());
    historyEventSummaryService.refresh(entity.getModification());
    super.createEntity(entity);
  }

  @Override
  protected void updateEntity(Announcement entity)
      throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getModification());
    super.updateEntity(entity);
  }

  @Override
  public void saveAnnouncement(Announcement announcement)
      throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(announcement);
    Objects.requireNonNull(announcement.getType());
    cleanAnnouncement(announcement);
    if (announcement.isNew()) {
      create(announcement);
    } else {
      update(announcement);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public void cleanWithoutSaving(Announcement announcement) {
    Optional.ofNullable(announcement)
        .filter(a -> a.getType() != null)
        .ifPresent(this::cleanAnnouncement);
  }

  @Override
  public List<Announcement> listEnabled() {
    return dao.listEnabled();
  }

  public boolean isOpen() {
    return Optional.ofNullable(userService.getAuthenticatedUser())
        .map(
            user -> {
              Instant lastActionDate = user.getAnnouncementInformation().getLastActionDate();
              LocalDateTime mostRecentPublicationStartDate =
                  dao.getMostRecentPublicationStartDate();

              if (lastActionDate == null
                  || LocalDateTime.ofInstant(lastActionDate, ZoneId.systemDefault())
                      .isBefore(mostRecentPublicationStartDate)) {
                return true;
              }
              return user.getAnnouncementInformation().isOpen();
            })
        .orElse(false);
  }

  @Override
  public void deleteAnnouncement(Announcement announcement)
      throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(announcement);
    delete(announcement);
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
}
