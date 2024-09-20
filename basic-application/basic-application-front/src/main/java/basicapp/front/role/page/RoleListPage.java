package basicapp.front.role.page;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_WRITE;
import static basicapp.front.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.search.RoleSort;
import basicapp.back.util.binding.Bindings;
import basicapp.front.role.model.RoleDataProvider;
import basicapp.front.role.template.RoleTemplate;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;

public class RoleListPage extends RoleTemplate {

  private static final long serialVersionUID = 1L;

  public static IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start()
        .validator(Condition.permission(GLOBAL_ROLE_READ))
        .page(RoleListPage.class);
  }

  @SpringBean private IPropertyService propertyService;

  public RoleListPage(PageParameters parameters) {
    super(parameters);

    EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");

    headerElementsSection
        .anyChildVisible()
        .add(
            new EnclosureContainer("actionsContainer")
                .anyChildVisible()
                .add(
                    RoleAddPage.linkDescriptor()
                        .link("add")
                        .add(Condition.permission(GLOBAL_ROLE_WRITE).thenShow())));

    RoleDataProvider dataProvider = new RoleDataProvider();

    DecoratedCoreDataTablePanel<Role, RoleSort> roles =
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addLabelColumn(new ResourceModel("business.role.title"), Bindings.role().title())
            .withLink(RoleDetailPage.MAPPER)
            .withClass("cell-w-250")
            .bootstrapCard()
            .ajaxPagers()
            .count("role.list.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));

    add(headerElementsSection, roles);
  }
}
