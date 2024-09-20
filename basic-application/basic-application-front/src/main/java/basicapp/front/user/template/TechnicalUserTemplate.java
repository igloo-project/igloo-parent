package basicapp.front.user.template;

import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;

import basicapp.front.user.page.TechnicalUserListPage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@AuthorizeInstantiation(ROLE_ADMIN)
public class TechnicalUserTemplate extends UserTemplate {
  private static final long serialVersionUID = 1L;

  public TechnicalUserTemplate(PageParameters parameters) {
    super(parameters);
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return TechnicalUserListPage.class;
  }
}
