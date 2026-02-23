package basicapp.back.business.announcement.service.controller;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.service.business.IAnnouncementService;
import igloo.loginmdc.annotation.LogExecution;
import java.util.List;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementControllerServiceImpl implements IAnnouncementControllerService {

  private final IAnnouncementService announcementService;

  @Autowired
  public AnnouncementControllerServiceImpl(IAnnouncementService announcementService) {
    this.announcementService = announcementService;
  }

  @Override
  @LogExecution
  public void saveAnnouncement(Announcement announcement)
      throws ServiceException, SecurityServiceException {
    announcementService.saveAnnouncement(announcement);
  }

  @Override
  @LogExecution
  public void deleteAnnouncement(Announcement announcement)
      throws SecurityServiceException, ServiceException {
    announcementService.deleteAnnouncement(announcement);
  }

  @Override
  public void cleanWithoutSaving(Announcement announcement) {
    announcementService.cleanWithoutSaving(announcement);
  }

  @Override
  public List<Announcement> listEnabled() {
    return announcementService.listEnabled();
  }

  @Override
  public boolean isOpen() {
    return announcementService.isOpen();
  }
}
