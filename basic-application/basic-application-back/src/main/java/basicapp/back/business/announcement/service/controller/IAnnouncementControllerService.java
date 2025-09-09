package basicapp.back.business.announcement.service.controller;

import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.ANNOUNCEMENT_REMOVE;
import static basicapp.back.security.model.BasicApplicationSecurityExpressionConstants.ANNOUNCEMENT_WRITE;

import basicapp.back.business.announcement.model.Announcement;
import java.util.List;
import org.iglooproject.commons.util.security.PermissionObject;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IAnnouncementControllerService {

  @PreAuthorize(ANNOUNCEMENT_WRITE)
  void saveAnnouncement(@PermissionObject Announcement announcement);

  @PreAuthorize(ANNOUNCEMENT_REMOVE)
  void deleteAnnouncement(@PermissionObject Announcement announcement);

  @PreAuthorize(ANNOUNCEMENT_WRITE)
  void cleanWithoutSaving(@PermissionObject Announcement announcement);

  List<Announcement> listEnabled();

  boolean isOpen();
}
