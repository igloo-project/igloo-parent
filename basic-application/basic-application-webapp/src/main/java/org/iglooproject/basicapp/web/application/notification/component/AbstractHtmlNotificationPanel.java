package org.iglooproject.basicapp.web.application.notification.component;

import igloo.bootstrap.IBootstrap4Component;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;

public abstract class AbstractHtmlNotificationPanel<T> extends GenericPanel<T>
    implements IBootstrap4Component {

  private static final long serialVersionUID = -3576134833190785445L;

  protected AbstractHtmlNotificationPanel(String id) {
    this(id, null);
  }

  protected AbstractHtmlNotificationPanel(String id, IModel<T> model) {
    super(id, model);

    add(
        new ExternalLink(
            "homePageLink",
            LoadableDetachableModel.of(
                () ->
                    BasicApplicationApplication.get()
                        .getHomePageLinkDescriptor()
                        .bypassPermissions()
                        .fullUrl())));
  }
}
