package basicapp.back.security.service.permission;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.user.model.User;
import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

public interface IAnnouncementPermissionEvaluator
    extends IGenericPermissionEvaluator<User, Announcement> {}
