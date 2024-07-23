package org.iglooproject.basicapp.web.application.navigation.link;

import org.iglooproject.wicket.more.link.factory.AbstractLinkFactory;

public final class LinkFactory extends AbstractLinkFactory {

  private static final LinkFactory INSTANCE = new LinkFactory();

  private LinkFactory() {}

  public static LinkFactory get() {
    return INSTANCE;
  }
}
