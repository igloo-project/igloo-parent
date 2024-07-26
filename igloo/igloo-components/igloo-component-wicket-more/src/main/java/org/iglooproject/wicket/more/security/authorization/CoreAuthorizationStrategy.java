package org.iglooproject.wicket.more.security.authorization;

import org.apache.wicket.authorization.strategies.CompoundAuthorizationStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.springframework.security.acls.domain.PermissionFactory;

public class CoreAuthorizationStrategy extends CompoundAuthorizationStrategy {

  public CoreAuthorizationStrategy(
      final IRoleCheckingStrategy roleCheckingStrategy,
      IAuthenticationService authenticationService,
      PermissionFactory permissionFactory) {
    add(new AnnotationsRoleAuthorizationStrategy(roleCheckingStrategy));
    add(new MetaDataRoleAuthorizationStrategy(roleCheckingStrategy));
    add(new AnnotationsPermissionAuthorizationStrategy(authenticationService, permissionFactory));
  }
}
