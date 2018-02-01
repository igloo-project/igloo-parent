package org.iglooproject.basicapp.web.application.administration.template;

import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.security.model.BasicApplicationPermissionConstants;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.component.UserSearchPanel;
import org.iglooproject.basicapp.web.application.administration.export.UserExcelTableExport;
import org.iglooproject.basicapp.web.application.administration.form.AbstractUserPopup;
import org.iglooproject.basicapp.web.application.administration.model.AbstractUserDataProvider;
import org.iglooproject.basicapp.web.application.common.renderer.ActionRenderers;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.basicapp.web.application.common.util.CssClassConstants;
import org.iglooproject.commons.util.functional.Predicates2;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.link.EmailLink;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.export.excel.component.AbstractExcelExportAjaxLink;
import org.iglooproject.wicket.more.export.excel.component.ExcelExportWorkInProgressModalPopupPanel;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.action.AbstractOneParameterAjaxAction;
import org.iglooproject.wicket.more.markup.html.factory.ModelFactories;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.rendering.BooleanRenderer;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public abstract class AdministrationUserPortfolioTemplate<U extends User> extends AdministrationTemplate {

	private static final long serialVersionUID = 1824247169136460059L;
	
	@SpringBean
	private IUserService userService;

	@SpringBean
	private IPropertyService propertyService;
	
	protected UserTypeDescriptor<U> typeDescriptor;
	
	public AdministrationUserPortfolioTemplate(PageParameters parameters, UserTypeDescriptor<U> typeDescriptor, IModel<String> pageTitleModel) {
		super(parameters);
		this.typeDescriptor = typeDescriptor;
		
		AbstractUserPopup<U> addPopup = createAddPopup("addPopup");
		
		final AbstractUserDataProvider<U> dataProvider = newDataProvider();
		DecoratedCoreDataTablePanel<U, ?> dataTablePanel = createDataTable("dataTable", dataProvider, propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));
		
		add(
				addPopup,
				new BlankLink("add")
						.add(
								new AjaxModalOpenBehavior(addPopup, MouseEvent.CLICK)
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
				
				new UserSearchPanel<>("search", dataTablePanel, typeDescriptor, dataProvider),
				dataTablePanel
		);
		
	}
	
	protected abstract AbstractUserDataProvider<U> newDataProvider();
	
	protected abstract AbstractUserPopup<U> createAddPopup(String wicketId);
	
	protected DecoratedCoreDataTablePanel<U, ?> createDataTable(String wicketId, final IDataProvider<U> dataProvider,
			int itemsPerPage) {
		PageModel<Page> pageModel = new PageModel<Page>(this);
		
		return DataTableBuilder.start(dataProvider)
				.addLabelColumn(new ResourceModel("business.user.username"), Bindings.user().username())
						.withClass("text text-md")
						.withLink(AdministrationUserDescriptionTemplate.<U>mapper().setParameter2(pageModel))
				.addLabelColumn(new ResourceModel("business.user.lastName"), Bindings.user().lastName())
						.withClass("text text-md")
				.addLabelColumn(new ResourceModel("business.user.firstName"), Bindings.user().firstName())
						.withClass("text text-md")
				.addColumn(new AbstractCoreColumn<U, ISort<?>>(new ResourceModel("business.user.email")) {
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
				.addBootstrapBadgeColumn(new ResourceModel("business.user.active"), Bindings.user().active(), BooleanRenderer.get())
						.withClass("icon")
				.addActionColumn()
						.addLink(ActionRenderers.view(), AdministrationUserDescriptionTemplate.<U>mapper().setParameter2(pageModel))
						.addConfirmAction(ActionRenderers.delete())
								.title(ModelFactories.stringResourceModel(
										"administration.user.delete.confirmation.title",
										Bindings.user().fullName()
								))
								.content(ModelFactories.stringResourceModel(
										"administration.user.delete.confirmation.text",
										Bindings.user().fullName()
								))
								.confirm()
								.onClick(new AbstractOneParameterAjaxAction<IModel<U>>() {
									private static final long serialVersionUID = 1L;
									@Override
									public void execute(AjaxRequestTarget target, IModel<U> parameter) {
										try {
											userService.delete(parameter.getObject()); 
											Session.get().success(getString("common.success"));
										} catch (Exception e) {
											Session.get().error(getString("common.error.unexpected"));
										}
										target.add(getPage());
										dataProvider.detach();
										FeedbackUtils.refreshFeedback(target, getPage());
									}
								})
								.whenPermission(BasicApplicationPermissionConstants.DELETE)
								.hidePlaceholder()
						.withClassOnElements(CssClassConstants.BTN_XS)
						.end()
						.withClass("actions actions-2x")
						
				.withNoRecordsResourceKey("administration.user.noUsers")
				.decorate()
						.ajaxPagers()
				.build(wicketId, itemsPerPage);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class<? extends AdministrationUserPortfolioTemplate> getSecondMenuPage() {
		return getClass();
	}
}
