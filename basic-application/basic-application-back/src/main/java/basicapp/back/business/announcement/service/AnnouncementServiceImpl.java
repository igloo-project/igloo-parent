package basicapp.back.business.announcement.service;

import basicapp.back.business.announcement.dao.IAnnouncementDao;
import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.history.service.IHistoryEventSummaryService;
import java.time.LocalDateTime;
import java.util.List;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl extends GenericEntityServiceImpl<Long, Announcement>
    implements IAnnouncementService {

  private IAnnouncementDao dao;

  @Autowired private IHistoryEventSummaryService historyEventSummaryService;

  public AnnouncementServiceImpl(IAnnouncementDao dao) {
    super(dao);
    this.dao = dao;
  }

  @Override
  protected void createEntity(Announcement entity)
      throws ServiceException, SecurityServiceException {
    cleanWithoutSaving(entity);
    historyEventSummaryService.refresh(entity.getCreation());
    historyEventSummaryService.refresh(entity.getModification());
    super.createEntity(entity);
  }

  @Override
  protected void updateEntity(Announcement entity)
      throws ServiceException, SecurityServiceException {
    cleanWithoutSaving(entity);
    historyEventSummaryService.refresh(entity.getModification());
    super.updateEntity(entity);
  }

  @Override
  public void saveAnnouncement(Announcement announcement)
      throws ServiceException, SecurityServiceException {
    if (announcement.isNew()) {
      create(announcement);
    } else {
      update(announcement);
    }
  }

  @Override
  public void cleanWithoutSaving(Announcement announcement) {
    if (announcement == null || announcement.getType() == null) {
      return;
    }

    switch (announcement.getType()) {
      case SERVICE_INTERRUPTION:
        announcement.setTitle(null);
        announcement.setDescription(null);
        break;
      case OTHER:
        announcement.setInterruption(null);
        break;
      default:
        throw new IllegalSwitchValueException(announcement.getType());
    }
  }

  @Override
  public List<Announcement> listEnabled() {
    return dao.listEnabled();
  }

  @Override
  public LocalDateTime getMostRecentPublicationStartDate() {
    return dao.getMostRecentPublicationStartDate();
  }
}
