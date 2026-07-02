package org.iglooproject.wicket.more.link.descriptor.generator;

import org.apache.wicket.Page;
import org.apache.wicket.model.IDetachable;
import org.iglooproject.wicket.more.link.descriptor.IBookmarkablePageLink;

public interface IBasicPageLinkGenerator extends IDetachable {

  IBookmarkablePageLink link(String wicketId);

  boolean isActive(Class<? extends Page> selectedPage);
}
