package org.iglooproject.wicket.more.security.authorization;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;

public class AnnotationsPermissionAuthorizationStrategy implements IAuthorizationStrategy {

  private IAuthenticationService authenticationService;

  private PermissionFactory permissionFactory;

  public AnnotationsPermissionAuthorizationStrategy(
      IAuthenticationService authenticationService, PermissionFactory permissionFactory) {
    this.authenticationService = authenticationService;
    this.permissionFactory = permissionFactory;
  }

  @Override
  public boolean isActionAuthorized(Component component, Action action) {
    final Class<? extends Component> componentClass = component.getClass();

    final AuthorizeActionIfPermission permissionAnnotation =
        componentClass.getAnnotation(AuthorizeActionIfPermission.class);
    if (permissionAnnotation != null) {
      if (action.getName().equals(permissionAnnotation.action())) {
        String[] permissionNames = permissionAnnotation.permissions();
        for (String permissionName : permissionNames) {
          Permission permission = permissionFactory.buildFromName(permissionName);
          if (authenticationService.hasPermission(permission)) {
            return true;
          }
        }
        return false;
      }
    }

    return true;
  }

  @Override
  public <T extends IRequestableComponent> boolean isInstantiationAuthorized(
      Class<T> componentClass) {
    final AuthorizeInstantiationIfPermission authorizeInstantiationAnnotation =
        componentClass.getAnnotation(AuthorizeInstantiationIfPermission.class);
    if (authorizeInstantiationAnnotation != null) {
      String[] permissionNames = authorizeInstantiationAnnotation.permissions();
      for (String permissionName : permissionNames) {
        Permission permission = permissionFactory.buildFromName(permissionName);
        if (authenticationService.hasPermission(permission)) {
          return true;
        }
      }
      return false;
    }

    return true;
  }

  @Override
  public boolean isResourceAuthorized(IResource resource, PageParameters parameters) {
    // TODO 0.10 : implémenter un truc intelligent là-dessus
    return true;
  }
}
