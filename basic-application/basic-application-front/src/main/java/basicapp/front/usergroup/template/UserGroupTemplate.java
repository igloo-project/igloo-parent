package basicapp.front.usergroup.template;

import basicapp.front.common.template.MainTemplate;
import basicapp.front.usergroup.page.UserGroupListPage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

@AuthorizeInstantiation(CoreAuthorityConstants.ROLE_ADMIN)
public class UserGroupTemplate extends MainTemplate {

  private static final long serialVersionUID = 1L;

  public UserGroupTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration")));

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.administration.userGroup"),
            UserGroupListPage.linkDescriptor()));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return UserGroupListPage.class;
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return UserGroupListPage.class;
  }
}
