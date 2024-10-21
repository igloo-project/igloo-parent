package basicapp.front.role.template;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_READ;

import basicapp.front.common.template.MainTemplate;
import basicapp.front.role.page.RoleListPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.security.authorization.AuthorizeInstantiationIfPermission;

@AuthorizeInstantiationIfPermission(permissions = GLOBAL_ROLE_READ)
public abstract class RoleTemplate extends MainTemplate {

  private static final long serialVersionUID = 1L;

  protected RoleTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration")));
    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.administration.role"), RoleListPage.linkDescriptor()));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return RoleListPage.class;
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return RoleListPage.class;
  }
}
