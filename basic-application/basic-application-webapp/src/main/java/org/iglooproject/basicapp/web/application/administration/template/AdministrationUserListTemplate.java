package org.iglooproject.basicapp.web.application.administration.template;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.core.business.user.model.User;

public abstract class AdministrationUserListTemplate<U extends User>
    extends AdministrationUserTemplate {

  private static final long serialVersionUID = -4147350569532927616L;

  public AdministrationUserListTemplate(PageParameters parameters) {
    super(parameters);
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return getClass();
  }
}
