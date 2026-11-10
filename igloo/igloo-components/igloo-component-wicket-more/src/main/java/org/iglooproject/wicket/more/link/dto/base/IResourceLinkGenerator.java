package org.iglooproject.wicket.more.link.dto.base;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.link.dto.component.ResourceLinkGeneratorLink;

/** Resource counterpart of {@link IPageLinkGenerator}. */
public interface IResourceLinkGenerator extends IDetachable {

  ResourceReference getResourceReference();

  IResourceLinkGenerator bypassPermissions();

  boolean isValid();

  ResourceLinkGeneratorLink link(String wicketId);

  String url();

  String fullUrl();
}
