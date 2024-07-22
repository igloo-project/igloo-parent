package org.iglooproject.wicket.more.link.descriptor.impl;

import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;

/**
 * An {@link AbstractDynamicBookmarkableLink} whose targeted {@link ResourceReference} and {@link
 * PageParameters} may change during the page life cycle.
 *
 * <p>This implementation could not derive from {@link ResourceLink}, whose target Resource is
 * inherently static.
 *
 * @see AbstractDynamicBookmarkableLink
 * @see ResourceLink
 */
public class DynamicBookmarkableResourceLink extends DynamicBookmarkableLink {

  private static final long serialVersionUID = 7217475839311474526L;

  public DynamicBookmarkableResourceLink(
      String wicketId, CoreResourceLinkDescriptorImpl linkGenerator) {
    super(wicketId, linkGenerator);
  }
}
