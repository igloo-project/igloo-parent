package org.iglooproject.wicket.more.security.authorization;

import java.util.Objects;
import java.util.function.Supplier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.iglooproject.wicket.more.link.dto.PageLinkDescriptorRegistry;
import org.iglooproject.wicket.more.link.dto.security.IPageLinkPermissionEvaluator;

/**
 * Checks page instantiation with the {@link IPageLinkPermissionEvaluator}, for pages registered in
 * the {@link PageLinkDescriptorRegistry} (pages mounted with their link descriptor).
 *
 * <p>The evaluator is called with a new data DTO (from the descriptor factory) and a {@code null}
 * permission: it must only check global access to the page. Access to a given object is checked
 * when extracting the data DTO from the page parameters.
 */
public class PageLinkPermissionAuthorizationStrategy implements IAuthorizationStrategy {

  private final PageLinkDescriptorRegistry descriptorRegistry;

  private final Supplier<IPageLinkPermissionEvaluator> permissionEvaluatorSupplier;

  public PageLinkPermissionAuthorizationStrategy(
      PageLinkDescriptorRegistry descriptorRegistry,
      Supplier<IPageLinkPermissionEvaluator> permissionEvaluatorSupplier) {
    this.descriptorRegistry = Objects.requireNonNull(descriptorRegistry);
    this.permissionEvaluatorSupplier = Objects.requireNonNull(permissionEvaluatorSupplier);
  }

  @Override
  public <T extends IRequestableComponent> boolean isInstantiationAuthorized(
      Class<T> componentClass) {
    if (!Page.class.isAssignableFrom(componentClass)) {
      return true;
    }
    IPageLinkPermissionEvaluator permissionEvaluator = permissionEvaluatorSupplier.get();
    if (permissionEvaluator == null) {
      return true;
    }
    // TODO RFO DTO -> rendre obligatoire la mise dans le registre avec les DTO -> orElse(false) ?
    return descriptorRegistry
        .getDescriptor(componentClass.asSubclass(Page.class))
        .map(descriptor -> permissionEvaluator.hasPermission(descriptor.newDataDto(), null))
        .orElse(true);
  }

  @Override
  public boolean isActionAuthorized(Component component, Action action) {
    return true;
  }

  @Override
  public boolean isResourceAuthorized(IResource resource, PageParameters parameters) {
    return true;
  }
}
