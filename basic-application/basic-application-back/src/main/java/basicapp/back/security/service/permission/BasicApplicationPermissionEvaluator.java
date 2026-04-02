package basicapp.back.security.service.permission;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.business.role.model.Role;
import basicapp.back.business.user.model.User;
import org.iglooproject.jpa.security.service.AbstractCorePermissionEvaluator;
import org.iglooproject.jpa.util.HibernateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;

public class BasicApplicationPermissionEvaluator extends AbstractCorePermissionEvaluator<User> {

  @Autowired private IUserPermissionEvaluator userPermissionEvaluator;

  @Autowired private IReferenceDataPermissionEvaluator referenceDataPermissionEvaluator;

  @Autowired private IAnnouncementPermissionEvaluator announcementPermissionEvaluator;

  @Autowired private IRolePermissionEvaluator rolePermissionEvaluator;

  @Override
  protected boolean hasPermission(User user, Object targetDomainObject, Permission permission) {
    if (targetDomainObject != null) {
      targetDomainObject = HibernateUtils.unwrap(targetDomainObject); // NOSONAR
    }

    if (user != null) {
      user = HibernateUtils.unwrap(user); // NOSONAR
    }

    return switch (targetDomainObject) {
      case User targetUser -> userPermissionEvaluator.hasPermission(user, targetUser, permission);
      case ReferenceData<?> referenceData ->
          referenceDataPermissionEvaluator.hasPermission(user, referenceData, permission);
      case Announcement announcement ->
          announcementPermissionEvaluator.hasPermission(user, announcement, permission);
      case Role role -> rolePermissionEvaluator.hasPermission(user, role, permission);
      case null, default -> false;
    };
  }
}
