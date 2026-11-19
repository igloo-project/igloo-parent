package basicapp.back.business.announcement.service.business;

import basicapp.back.business.announcement.model.Announcement;
import java.util.List;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.jspecify.annotations.Nullable;

public interface IAnnouncementService extends IGenericEntityService<Long, Announcement> {

  void saveAnnouncement(Announcement announcement)
      throws ServiceException, SecurityServiceException;

  void cleanWithoutSaving(@Nullable Announcement announcement);

  List<Announcement> listEnabled();

  boolean isOpen();

  void deleteAnnouncement(Announcement announcement)
      throws ServiceException, SecurityServiceException;
}
