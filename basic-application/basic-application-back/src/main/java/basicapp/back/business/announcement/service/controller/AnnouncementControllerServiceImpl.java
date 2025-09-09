package basicapp.back.business.announcement.service.controller;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.service.business.IAnnouncementService;
import java.util.List;
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
  public void saveAnnouncement(Announcement announcement) {
    announcementService.saveAnnouncement(announcement);
  }

  @Override
  public void deleteAnnouncement(Announcement announcement) {
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
