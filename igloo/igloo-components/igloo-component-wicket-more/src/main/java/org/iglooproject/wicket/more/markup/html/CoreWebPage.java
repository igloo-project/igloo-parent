package org.iglooproject.wicket.more.markup.html;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class CoreWebPage extends WebPage {

  private static final long serialVersionUID = 607411189304353902L;

  public CoreWebPage() {}

  public CoreWebPage(IModel<?> model) {
    super(model);
  }

  public CoreWebPage(PageParameters parameters) {
    super(parameters);
  }

  protected boolean isPageAccessible(Class<? extends Page> pageClass) {
    return Session.get().getAuthorizationStrategy().isInstantiationAuthorized(pageClass);
  }
}
