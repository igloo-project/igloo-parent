package basicapp.front.role.page;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_READ;

import basicapp.back.business.role.model.Role;
import basicapp.front.role.component.RoleSaveFooterPanel;
import basicapp.front.role.component.RoleSavePermissionsPanel;
import basicapp.front.role.component.RoleSaveTitlePanel;
import basicapp.front.role.template.RoleTemplate;
import igloo.wicket.condition.Condition;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class RoleAddPage extends RoleTemplate {

  private static final long serialVersionUID = 1L;

  public static IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start()
        .validator(Condition.permission(GLOBAL_ROLE_READ))
        .page(RoleAddPage.class);
  }

  public RoleAddPage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.administration.role.add"), RoleAddPage.linkDescriptor()));

    IModel<Role> roleModel = new GenericEntityModel<>(new Role());

    Form<Role> form = new Form<>("form", roleModel);
    add(form);

    form.add(
        new RoleSaveTitlePanel("title", roleModel),
        new RoleSavePermissionsPanel("permissions", roleModel),
        new RoleSaveFooterPanel("footer", roleModel));
  }

  @Override
  protected Condition displayBreadcrumb() {
    return Condition.alwaysFalse();
  }
}
