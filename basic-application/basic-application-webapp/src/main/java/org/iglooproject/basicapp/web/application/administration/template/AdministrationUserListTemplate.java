package org.iglooproject.basicapp.web.application.administration.template;

import static org.iglooproject.basicapp.web.application.common.util.CssClassConstants.BTN_XS;
import static org.iglooproject.basicapp.web.application.common.util.CssClassConstants.CELL_HIDDEN_MD_AND_LESS;
import static org.iglooproject.basicapp.web.application.common.util.CssClassConstants.ROW_DISABLED;
import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.search.UserSort;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.component.UserListSearchPanel;
import org.iglooproject.basicapp.web.application.administration.export.UserExcelTableExport;
import org.iglooproject.basicapp.web.application.administration.form.AbstractUserPopup;
import org.iglooproject.basicapp.web.application.administration.model.AbstractUserDataProvider;
import org.iglooproject.basicapp.web.application.common.renderer.ActionRenderers;
import org.iglooproject.basicapp.web.application.common.renderer.UserActiveRenderer;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.link.EmailLink;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.export.excel.component.AbstractExcelExportAjaxLink;
import org.iglooproject.wicket.more.export.excel.component.ExcelExportWorkInProgressModalPopupPanel;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.iglooproject.wicket.more.model.BindingModel;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public abstract class AdministrationUserListTemplate<U extends User> extends AdministrationTemplate {

	private static final long serialVersionUID = 1824247169136460059L;
	
	@SpringBean
	private IUserService userService;

	@SpringBean
	private IPropertyService propertyService;
	
	protected UserTypeDescriptor<U> typeDescriptor;
	
	public AdministrationUserListTemplate(PageParameters parameters, UserTypeDescriptor<U> typeDescriptor, IModel<String> pageTitleModel) {
		super(parameters);
		this.typeDescriptor = typeDescriptor;
		
		AbstractUserPopup<U> addPopup = createAddPopup("addPopup");
		
		final AbstractUserDataProvider<U> dataProvider = newDataProvider();
		DecoratedCoreDataTablePanel<U, ?> dataTablePanel = createDataTable("dataTable", dataProvider, propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));
		
		add(
				addPopup,
				new BlankLink("add")
						.add(
								new AjaxModalOpenBehavior(addPopup, MouseEvent.CLICK) {
									private static final long serialVersionUID = 1L;
									@Override
									protected void onShow(AjaxRequestTarget target) {
										addPopup.setUpAdd(typeDescriptor.administrationTypeDescriptor().newInstance());
									}
								}
						)
		);
		
		ExcelExportWorkInProgressModalPopupPanel loadingPopup = new ExcelExportWorkInProgressModalPopupPanel("loadingPopup");
		add(
				loadingPopup,
				new AbstractExcelExportAjaxLink("exportExcel", loadingPopup, "export-users-") {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected Workbook generateWorkbook() {
						UserExcelTableExport export = new UserExcelTableExport(this);
						return export.generate(dataProvider);
					}
				}
		);
		
		add(
				new CoreLabel("title", pageTitleModel),
				
				new UserListSearchPanel<>("search", dataTablePanel, typeDescriptor, dataProvider),
				dataTablePanel
		);
		
	}
	
	protected abstract AbstractUserDataProvider<U> newDataProvider();
	
	protected abstract AbstractUserPopup<U> createAddPopup(String wicketId);
	
	protected DecoratedCoreDataTablePanel<U, ?> createDataTable(String wicketId, final AbstractUserDataProvider<U> dataProvider, int itemsPerPage) {
		PageModel<Page> pageModel = new PageModel<Page>(this);
		
		return DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
				.addBootstrapBadgeColumn(Model.of(), Bindings.user(), UserActiveRenderer.get())
						.hideLabel()
						.withClass("narrow")
				.addLabelColumn(new ResourceModel("business.user.username"), Bindings.user().username())
						.withLink(AdministrationUserDetailTemplate.<U>mapper().setParameter2(pageModel))
						.withClass("text text-md")
				.addLabelColumn(new ResourceModel("business.user.lastName"), Bindings.user().lastName())
						.withSideLink(AdministrationUserDetailTemplate.<U>mapper().setParameter2(pageModel))
						.withSort(UserSort.LAST_NAME, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
						.withClass("text text-md")
				.addLabelColumn(new ResourceModel("business.user.firstName"), Bindings.user().firstName())
						.withSort(UserSort.FIRST_NAME, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
						.withClass("text text-md")
				.addColumn(new AbstractCoreColumn<U, UserSort>(new ResourceModel("business.user.email")) {
					private static final long serialVersionUID = 1L;
					@Override
					public void populateItem(Item<ICellPopulator<U>> cellItem, String componentId, IModel<U> rowModel) {
						IModel<String> emailModel = BindingModel.of(rowModel, Bindings.user().email());
						cellItem.add(
								new EmailLink(componentId, emailModel) {
									private static final long serialVersionUID = 1L;
									@Override
									protected void onComponentTag(ComponentTag tag) {
										tag.setName("a");
										super.onComponentTag(tag);
									}
								}
								.add(Condition.predicate(emailModel, Predicates2.hasText()).thenShow())
						);
					}
				})
						.withClass("text text-md")
						.withClass(CELL_HIDDEN_MD_AND_LESS)
				.addActionColumn()
						.addLink(ActionRenderers.view(), AdministrationUserDetailTemplate.<U>mapper().setParameter2(pageModel))
						.withClassOnElements(BTN_XS)
						.end()
						.withClass("actions actions-1x")
				.addRowCssClass((user) -> (user != null && !user.isActive()) ? ROW_DISABLED : null)
				.withNoRecordsResourceKey("administration.user.list.count.zero")
				.decorate()
						.ajaxPagers()
						.count("administration.user.list.count")
				.build(wicketId, itemsPerPage);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class<? extends AdministrationUserListTemplate> getSecondMenuPage() {
		return getClass();
	}
}
