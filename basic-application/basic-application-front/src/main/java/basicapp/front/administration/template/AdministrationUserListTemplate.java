package basicapp.front.administration.template;

import basicapp.back.business.user.model.User;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

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
