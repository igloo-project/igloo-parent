package basicapp.back.business.announcement.service.business;

import basicapp.back.business.announcement.model.Announcement;
import java.util.List;

public interface IAnnouncementService {

  void saveAnnouncement(Announcement announcement);

  void cleanWithoutSaving(Announcement announcement);

  List<Announcement> listEnabled();

  boolean isOpen();

  void deleteAnnouncement(Announcement announcement);
}
