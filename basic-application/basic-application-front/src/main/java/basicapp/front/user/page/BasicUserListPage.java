package basicapp.front.user.page;

import static basicapp.front.common.util.CssClassConstants.CELL_DISPLAY_2XL;
import static basicapp.front.common.util.CssClassConstants.TABLE_ROW_DISABLED;
import static basicapp.front.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import basicapp.back.business.user.model.BasicUser;
import basicapp.back.business.user.predicate.UserPredicates;
import basicapp.back.business.user.search.BasicUserSort;
import basicapp.back.business.user.service.IUserService;
import basicapp.back.util.binding.Bindings;
import basicapp.front.user.export.UserExcelTableExport;
import basicapp.front.user.model.BasicUserDataProvider;
import basicapp.front.user.panel.BasicUserListSearchPanel;
import basicapp.front.user.popup.BasicUserPopup;
import basicapp.front.user.renderer.UserEnabledRenderer;
import basicapp.front.user.template.UserListTemplate;
import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.link.EmailLink;
import igloo.wicket.model.BindingModel;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.excel.AbstractExcelExportAjaxLink;
import org.iglooproject.wicket.more.excel.ExcelExportWorkInProgressModalPopupPanel;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class BasicUserListPage extends UserListTemplate<BasicUser> {

  private static final long serialVersionUID = 8009837891166387406L;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(BasicUserListPage.class);
  }

  @SpringBean private IUserService userService;

  @SpringBean private IPropertyService propertyService;

  public BasicUserListPage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("navigation.administration.basicUser")));

    BasicUserDataProvider dataProvider = new BasicUserDataProvider();

    BasicUserPopup addPopup = new BasicUserPopup("addPopup");
    add(addPopup);

    ExcelExportWorkInProgressModalPopupPanel loadingPopup =
        new ExcelExportWorkInProgressModalPopupPanel("loadingPopup");
    add(loadingPopup);

    EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
    add(headerElementsSection.anyChildVisible());

    headerElementsSection.add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                new AbstractExcelExportAjaxLink("exportExcel", loadingPopup, "export-users-") {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected Workbook generateWorkbook() {
                    UserExcelTableExport export = new UserExcelTableExport(this);
                    return export.generate(dataProvider);
                  }
                },
                new BlankLink("add")
                    .add(
                        new AjaxModalOpenBehavior(addPopup, MouseEvent.CLICK) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void onShow(AjaxRequestTarget target) {
                            addPopup.setUpAdd(new BasicUser());
                          }
                        })));

    DecoratedCoreDataTablePanel<BasicUser, ?> results =
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addBootstrapBadgeColumn(Model.of(), Bindings.user(), UserEnabledRenderer.get())
            .badgePill()
            .withClass("cell-w-100 text-center")
            .addLabelColumn(new ResourceModel("business.user.username"), Bindings.user().username())
            .withLink(BasicUserDetailPage.MAPPER.setParameter2(new PageModel<>(this)))
            .withClass("cell-w-250")
            .addLabelColumn(new ResourceModel("business.user.lastName"), Bindings.user().lastName())
            .withSort(BasicUserSort.LAST_NAME, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addLabelColumn(
                new ResourceModel("business.user.firstName"), Bindings.user().firstName())
            .withSort(BasicUserSort.FIRST_NAME, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addColumn(
                new AbstractCoreColumn<BasicUser, BasicUserSort>(
                    new ResourceModel("business.user.email")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<BasicUser>> cellItem,
                      String componentId,
                      IModel<BasicUser> rowModel) {
                    IModel<String> emailModel = BindingModel.of(rowModel, Bindings.user().email());
                    cellItem.add(
                        new EmailLink(componentId, emailModel) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void onComponentTag(ComponentTag tag) {
                            tag.setName("a");
                            super.onComponentTag(tag);
                          }
                        }.add(Condition.predicate(emailModel, Predicates2.hasText()).thenShow()));
                  }
                })
            .withClass("cell-w-350")
            .withClass(CELL_DISPLAY_2XL)
            .rows()
            .withClass(
                itemModel ->
                    Condition.predicate(itemModel, UserPredicates.disabled())
                        .then(TABLE_ROW_DISABLED)
                        .otherwise(""))
            .end()
            .bootstrapCard()
            .ajaxPagers()
            .count("user.common.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));

    add(new BasicUserListSearchPanel("search", results, dataProvider.getDataModel()), results);
  }
}