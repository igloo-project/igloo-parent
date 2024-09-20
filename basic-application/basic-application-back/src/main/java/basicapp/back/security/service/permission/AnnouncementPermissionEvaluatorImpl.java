package basicapp.back.security.service.permission;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.ANNOUNCEMENT_REMOVE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.ANNOUNCEMENT_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ANNOUNCEMENT_WRITE;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.user.model.User;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementPermissionEvaluatorImpl
    extends AbstractGenericPermissionEvaluator<Announcement>
    implements IAnnouncementPermissionEvaluator {

  @Override
  public boolean hasPermission(User user, Announcement announcement, Permission permission) {

    if (is(permission, ANNOUNCEMENT_WRITE)) {
      return canWriteAnnouncement(user);
    }
    if (is(permission, ANNOUNCEMENT_REMOVE)) {
      return canRemoveAnnouncement(user);
    }

    return false;
  }

  @VisibleForTesting
  public boolean canWriteAnnouncement(User user) {
    return hasPermission(user, GLOBAL_ANNOUNCEMENT_WRITE);
  }

  @VisibleForTesting
  public boolean canRemoveAnnouncement(User user) {
    return hasPermission(user, GLOBAL_ANNOUNCEMENT_WRITE);
  }
}
