package basicapp.front.user.template;

import basicapp.back.security.model.BasicApplicationPermissionConstants;
import basicapp.front.common.template.MainTemplate;
import basicapp.front.user.page.BasicUserListPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.security.authorization.AuthorizeInstantiationIfPermission;

@AuthorizeInstantiationIfPermission(
    permissions = BasicApplicationPermissionConstants.GLOBAL_USER_READ)
public abstract class UserTemplate extends MainTemplate {

  private static final long serialVersionUID = 1L;

  public UserTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration")));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return BasicUserListPage.class;
  }
}
