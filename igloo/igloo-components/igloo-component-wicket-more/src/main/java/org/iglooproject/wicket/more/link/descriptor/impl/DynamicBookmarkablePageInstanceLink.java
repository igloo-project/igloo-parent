package org.iglooproject.wicket.more.link.descriptor.impl;

public class DynamicBookmarkablePageInstanceLink extends DynamicBookmarkableLink {

  private static final long serialVersionUID = 3375563675399910552L;

  public DynamicBookmarkablePageInstanceLink(
      String wicketId, CorePageInstanceLinkGenerator pageInstanceLinkGenerator) {
    super(wicketId, pageInstanceLinkGenerator);
  }
}
