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

    if (targetDomainObject instanceof User) {
      return userPermissionEvaluator.hasPermission(user, (User) targetDomainObject, permission);
    }
    if (targetDomainObject instanceof ReferenceData) {
      return referenceDataPermissionEvaluator.hasPermission(
          user, (ReferenceData<?>) targetDomainObject, permission);
    }
    if (targetDomainObject instanceof Announcement) {
      return announcementPermissionEvaluator.hasPermission(
          user, (Announcement) targetDomainObject, permission);
    }
    if (targetDomainObject instanceof Role) {
      return rolePermissionEvaluator.hasPermission(user, (Role) targetDomainObject, permission);
    }

    return false;
  }
}
