package basicapp.front.user.template;

import basicapp.front.common.template.MainTemplate;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

@AuthorizeInstantiation(CoreAuthorityConstants.ROLE_ADMIN)
public abstract class UserTemplate extends MainTemplate {

  private static final long serialVersionUID = 1L;

  public UserTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration")));
  }
}
