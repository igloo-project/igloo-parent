package basicapp.back.security.service;

import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;
import static org.iglooproject.jpa.security.model.CorePermissionConstants.CREATE;
import static org.iglooproject.jpa.security.model.CorePermissionConstants.READ;
import static org.iglooproject.jpa.security.model.CorePermissionConstants.WRITE;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.business.referencedata.predicate.ReferenceDataPredicates;
import basicapp.back.business.user.model.User;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

@Service
public class ReferenceDataPermissionEvaluatorImpl
    extends AbstractGenericPermissionEvaluator<ReferenceData<?>>
    implements IReferenceDataPermissionEvaluator {

  @Override
  public boolean hasPermission(User user, ReferenceData<?> referenceData, Permission permission) {
    if (is(permission, READ)) {
      return true;
    } else if (is(permission, CREATE)) {
      return hasRole(user, ROLE_ADMIN);
    } else if (is(permission, WRITE)) {
      return hasRole(user, ROLE_ADMIN) && ReferenceDataPredicates.editable().apply(referenceData);
    }

    return false;
  }
}
