package basicapp.front.user.page;

import static basicapp.front.common.util.CssClassConstants.BTN_TABLE_ROW_ACTION;
import static basicapp.front.common.util.CssClassConstants.CELL_DISPLAY_2XL;
import static basicapp.front.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import basicapp.back.business.user.model.TechnicalUser;
import basicapp.back.business.user.predicate.UserPredicates;
import basicapp.back.business.user.search.TechnicalUserSort;
import basicapp.back.business.user.service.IUserService;
import basicapp.back.util.binding.Bindings;
import basicapp.front.user.export.UserExcelTableExport;
import basicapp.front.user.model.TechnicalUserDataProvider;
import basicapp.front.user.panel.TechnicalUserListSearchPanel;
import basicapp.front.user.popup.TechnicalUserPopup;
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

public class TechnicalUserListPage extends UserListTemplate<TechnicalUser> {

  private static final long serialVersionUID = -4333618200257840036L;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(TechnicalUserListPage.class);
  }

  @SpringBean private IUserService userService;

  @SpringBean private IPropertyService propertyService;

  public TechnicalUserListPage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("navigation.administration.technicalUser")));

    TechnicalUserDataProvider dataProvider = new TechnicalUserDataProvider();

    TechnicalUserPopup addPopup = new TechnicalUserPopup("addPopup");
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
                            addPopup.setUpAdd(new TechnicalUser());
                          }
                        })));

    DecoratedCoreDataTablePanel<TechnicalUser, ?> results =
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addBootstrapBadgeColumn(Model.of(), Bindings.user(), UserEnabledRenderer.get())
            .badgePill()
            .withClass("cell-w-100 text-center")
            .addLabelColumn(new ResourceModel("business.user.username"), Bindings.user().username())
            .withLink(TechnicalUserDetailPage.MAPPER.setParameter2(new PageModel<>(this)))
            .withClass("cell-w-250")
            .addLabelColumn(new ResourceModel("business.user.lastName"), Bindings.user().lastName())
            .withSort(
                TechnicalUserSort.LAST_NAME, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addLabelColumn(
                new ResourceModel("business.user.firstName"), Bindings.user().firstName())
            .withSort(
                TechnicalUserSort.FIRST_NAME, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addColumn(
                new AbstractCoreColumn<TechnicalUser, TechnicalUserSort>(
                    new ResourceModel("business.user.email")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<TechnicalUser>> cellItem,
                      String componentId,
                      IModel<TechnicalUser> rowModel) {
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
                        .then(BTN_TABLE_ROW_ACTION)
                        .otherwise(""))
            .end()
            .bootstrapCard()
            .ajaxPagers()
            .count("user.common.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));

    add(new TechnicalUserListSearchPanel("search", results, dataProvider.getDataModel()), results);
  }
}
