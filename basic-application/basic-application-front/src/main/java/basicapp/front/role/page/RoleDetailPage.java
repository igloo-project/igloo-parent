package basicapp.front.role.page;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.ROLE_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.ROLE_WRITE;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.service.IRoleService;
import basicapp.back.security.model.BasicApplicationPermissionCategoryEnum;
import basicapp.back.security.model.BasicApplicationPermissionUtils;
import basicapp.back.util.binding.Bindings;
import basicapp.front.common.renderer.PermissionNameRenderer;
import basicapp.front.role.template.RoleTemplate;
import basicapp.front.user.page.BasicUserListPage;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Models;
import igloo.wicket.model.ReadOnlyMapModel;
import java.util.List;
import java.util.Map;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.markup.repeater.map.MapItem;
import org.iglooproject.wicket.more.markup.repeater.map.MapView;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.rendering.EnumRenderer;

public class RoleDetailPage extends RoleTemplate {

  private static final long serialVersionUID = 1L;

  public static final IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, Role> MAPPER =
      LinkDescriptorBuilder.start()
          .model(Role.class)
          .permission(ROLE_READ)
          .map(CommonParameters.ID)
          .mandatory()
          .page(RoleDetailPage.class);

  @SpringBean protected IRoleService roleService;

  public RoleDetailPage(PageParameters parameters) {
    super(parameters);

    IModel<Role> roleModel = new GenericEntityModel<>();

    addBreadCrumbElement(
        new BreadCrumbElement(BindingModel.of(roleModel, Bindings.role().title())));

    MAPPER
        .map(roleModel)
        .extractSafely(
            parameters, BasicUserListPage.linkDescriptor(), getString("common.error.unexpected"));

    IModel<Map<BasicApplicationPermissionCategoryEnum, List<String>>> permissionsModel =
        LoadableDetachableModel.of(() -> BasicApplicationPermissionUtils.PERMISSIONS);

    EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
    add(headerElementsSection.anyChildVisible());

    headerElementsSection.add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                RoleEditPage.MAPPER
                    .map(roleModel)
                    .link("edit")
                    .add(Condition.permission(roleModel, ROLE_WRITE).thenShow())));

    add(
        new CoreLabel("role", BindingModel.of(roleModel, Bindings.role())),
        new MapView<>(
            "categories",
            ReadOnlyMapModel.of(permissionsModel, Models.serializableModelFactory())) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void populateItem(
              MapItem<BasicApplicationPermissionCategoryEnum, List<String>> item) {
            item.add(
                new CoreLabel(
                    "category", EnumRenderer.get().render(item.getModelObject(), getLocale())),
                new CollectionView<>(
                    "permissions", item.getValueModel(), Models.serializableModelFactory()) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected void populateItem(Item<String> item) {
                    item.add(
                        new CoreLabel(
                            "permission",
                            PermissionNameRenderer.get()
                                .render(item.getModelObject(), getLocale())),
                        new WebMarkupContainer("icon")
                            .add(
                                new ClassAttributeAppender(
                                    Condition.contains(
                                            BindingModel.of(
                                                roleModel, Bindings.role().permissions()),
                                            item.getModel())
                                        .then("fa-circle-check text-success")
                                        .otherwise("fa-xmark text-danger"))));
                  }
                });
          }
        });
  }
}
