package org.iglooproject.wicket.more.notification.service;

import org.apache.wicket.markup.ComponentTag;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.css.ICssResourceReference;
import org.jsoup.nodes.Node;

public interface IHtmlNotificationCssService {

  void registerStyles(String componentVariation, ICssResourceReference cssResourceReference)
      throws ServiceException;

  IHtmlNotificationCssRegistry getRegistry(String componentVariation) throws ServiceException;

  String getCss(String componentVariation) throws ServiceException;

  interface IHtmlNotificationCssRegistry {

    String getStyle(ComponentTag tag);

    String getStyle(Node node);
  }

  void registerDefaultStyles(ICssResourceReference cssResourceReference);
}
