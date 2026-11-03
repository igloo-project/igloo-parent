package org.iglooproject.wicket.more.link.dto;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.wicket.Page;
import org.iglooproject.wicket.more.link.dto.base.IPageLinkDescriptor;

/**
 * Registry of the link descriptor of each page mounted with a {@link IPageLinkDescriptor} (see
 * {@code CoreWicketAuthenticatedApplication#mountPage(String, IPageLinkDescriptor)}). Used to check
 * page instantiation with the {@link
 * org.iglooproject.wicket.more.link.dto.security.IPageLinkPermissionEvaluator}, instead of security
 * annotations on pages.
 *
 * <p>One registry is held by each application instance.
 *
 * <p>Only the first descriptor registered for a page class is kept: all descriptors of a page are
 * expected to share the same data DTO type.
 */
public class PageLinkDescriptorRegistry {

  private final Map<Class<? extends Page>, IPageLinkDescriptor<?, ?>> descriptors =
      new ConcurrentHashMap<>();

  public void register(IPageLinkDescriptor<?, ?> descriptor) {
    Objects.requireNonNull(descriptor);
    descriptors.putIfAbsent(descriptor.getPageClass(), descriptor);
  }

  /**
   * @return the link descriptor of the page, empty if the page has not been mounted with a link
   *     descriptor.
   */
  public Optional<IPageLinkDescriptor<?, ?>> getDescriptor(Class<? extends Page> pageClass) {
    return Optional.ofNullable(descriptors.get(pageClass));
  }
}
