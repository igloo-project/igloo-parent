package basicapp.front.user.page;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_USER_READ;
import static basicapp.front.common.util.CssClassConstants.CELL_DISPLAY_2XL;
import static basicapp.front.common.util.CssClassConstants.TABLE_ROW_DISABLED;
import static basicapp.front.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.predicate.UserPredicates;
import basicapp.back.business.user.search.IUserSearchQuery;
import basicapp.back.business.user.search.UserSort;
import basicapp.back.business.user.service.controller.IUserControllerService;
import basicapp.back.util.binding.Bindings;
import basicapp.front.user.component.BasicUserListSearchPanel;
import basicapp.front.user.export.UserExcelTableExport;
import basicapp.front.user.model.UserDataProvider;
import basicapp.front.user.popup.BasicUserSavePopup;
import basicapp.front.user.renderer.UserEnabledRenderer;
import basicapp.front.user.template.UserTemplate;
import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.link.EmailLink;
import igloo.wicket.model.BindingModel;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebPage;
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

public class BasicUserListPage extends UserTemplate {

  private static final long serialVersionUID = 1L;

  public static IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start()
        .validator(Condition.permission(GLOBAL_USER_READ))
        .page(BasicUserListPage.class);
  }

  @SpringBean private IUserSearchQuery userSearchQuery;

  @SpringBean private IUserControllerService userControllerService;

  @SpringBean private IPropertyService propertyService;

  public BasicUserListPage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("navigation.administration.basicUser")));

    UserDataProvider dataProvider = new UserDataProvider();
    dataProvider.getDataModel().getObject().setType(UserType.BASIC);

    BasicUserSavePopup addPopup = new BasicUserSavePopup("addPopup");
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
                            addPopup.setUpAdd(new User());
                          }
                        })
                    .add(Condition.permission(GLOBAL_USER_READ).thenShow())));

    DecoratedCoreDataTablePanel<User, ?> results =
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addBootstrapBadgeColumn(Model.of(), Bindings.user(), UserEnabledRenderer.get())
            .badgePill()
            .withClass("cell-w-100 text-center")
            .addLabelColumn(new ResourceModel("business.user.username"), Bindings.user().username())
            .withLink(BasicUserDetailPage.MAPPER.setParameter2(new PageModel<>(this)))
            .withClass("cell-w-250")
            .addLabelColumn(new ResourceModel("business.user.lastName"), Bindings.user().lastName())
            .withSort(UserSort.LAST_NAME, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addLabelColumn(
                new ResourceModel("business.user.firstName"), Bindings.user().firstName())
            .withSort(UserSort.FIRST_NAME, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addColumn(
                new AbstractCoreColumn<>(new ResourceModel("business.user.email")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<User>> cellItem,
                      String componentId,
                      IModel<User> rowModel) {
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

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return BasicUserListPage.class;
  }
}
