package basicapp.front.user.template;

import basicapp.back.business.user.model.User;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class UserListTemplate<U extends User> extends UserTemplate {

  private static final long serialVersionUID = -4147350569532927616L;

  public UserListTemplate(PageParameters parameters) {
    super(parameters);
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return getClass();
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return getClass();
  }
}
