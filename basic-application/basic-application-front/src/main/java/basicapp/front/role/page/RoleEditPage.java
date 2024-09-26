package basicapp.front.role.page;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.ROLE_WRITE;

import basicapp.back.business.role.model.Role;
import basicapp.back.util.binding.Bindings;
import basicapp.front.role.component.RoleSaveFooterPanel;
import basicapp.front.role.component.RoleSavePermissionsPanel;
import basicapp.front.role.component.RoleSaveTitlePanel;
import basicapp.front.role.template.RoleTemplate;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class RoleEditPage extends RoleTemplate {

  private static final long serialVersionUID = 1L;

  public static final IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, Role> MAPPER =
      LinkDescriptorBuilder.start()
          .model(Role.class)
          .permission(ROLE_WRITE)
          .map(CommonParameters.ID)
          .mandatory()
          .page(RoleEditPage.class);

  public RoleEditPage(PageParameters parameters) {
    super(parameters);

    IModel<Role> roleModel = new GenericEntityModel<>();

    MAPPER
        .map(roleModel)
        .extractSafely(
            parameters, RoleListPage.linkDescriptor(), getString("common.error.unexpected"));

    addBreadCrumbElement(
        new BreadCrumbElement(
            new StringResourceModel("navigation.administration.role.edit", roleModel),
            RoleDetailPage.MAPPER.map(roleModel)));

    Form<Role> form = new Form<>("form", roleModel);

    form.add(
        new RoleSaveTitlePanel("title", roleModel),
        new RoleSavePermissionsPanel("permissions", roleModel),
        new RoleSaveFooterPanel("footer", roleModel));

    add(new CoreLabel("role", BindingModel.of(roleModel, Bindings.role())), form);
  }
}
