package fr.openwide.core.basicapp.web.application.administration.template;

import static fr.openwide.core.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.security.model.BasicApplicationPermissionConstants;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.administration.component.UserSearchPanel;
import fr.openwide.core.basicapp.web.application.administration.export.UserExcelTableExport;
import fr.openwide.core.basicapp.web.application.administration.form.AbstractUserPopup;
import fr.openwide.core.basicapp.web.application.administration.model.AbstractUserDataProvider;
import fr.openwide.core.basicapp.web.application.common.renderer.ActionRenderers;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.spring.property.service.IPropertyService;
import fr.openwide.core.wicket.markup.html.link.EmailLink;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.export.excel.component.AbstractExcelExportAjaxLink;
import fr.openwide.core.wicket.more.export.excel.component.ExcelExportWorkInProgressModalPopupPanel;
import fr.openwide.core.wicket.more.link.model.PageModel;
import fr.openwide.core.wicket.more.markup.html.action.AbstractOneParameterAjaxAction;
import fr.openwide.core.wicket.more.markup.html.factory.ModelFactories;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.link.BlankLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import fr.openwide.core.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import fr.openwide.core.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.rendering.BooleanRenderer;

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
		DecoratedCoreDataTablePanel<U, ?> dataTablePanel =
				createDataTable("dataTable", dataProvider, propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));
		
		add(
				new Label("title", pageTitleModel),
				
				new UserSearchPanel<>("searchPanel", dataTablePanel, typeDescriptor, dataProvider),
				
				dataTablePanel,
				
				addPopup,
				new BlankLink("addButton")
						.add(
								new AjaxModalOpenBehavior(addPopup, MouseEvent.CLICK)
						)
		);
		
		// Export Excel
		ExcelExportWorkInProgressModalPopupPanel loadingPopup = new ExcelExportWorkInProgressModalPopupPanel("loadingPopup");
		add(
				loadingPopup,
				new AbstractExcelExportAjaxLink("exportExcelButton", loadingPopup, "export-users-") {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected Workbook generateWorkbook() {
						UserExcelTableExport export = new UserExcelTableExport(this);
						return export.generate(dataProvider);
					}
				}
		);
	}
	
	protected abstract AbstractUserDataProvider<U> newDataProvider();
	
	protected abstract AbstractUserPopup<U> createAddPopup(String wicketId);
	
	protected DecoratedCoreDataTablePanel<U, ?> createDataTable(String wicketId, final IDataProvider<U> dataProvider,
			int itemsPerPage) {
		PageModel<Page> pageModel = new PageModel<Page>(this);
		
		return DataTableBuilder.start(dataProvider)
				.addLabelColumn(new ResourceModel("business.user.userName"), Bindings.user().userName())
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
											Session.get().success(getString("common.delete.success"));
										} catch (Exception e) {
											Session.get().error(getString("common.delete.error"));
										}
										target.add(getPage());
										dataProvider.detach();
										FeedbackUtils.refreshFeedback(target, getPage());
									}
									@Override
									public Condition getActionAvailableCondition(IModel<U> parameter) {
										return Condition.permission(parameter, BasicApplicationPermissionConstants.DELETE);
									}
								})
								.hidePlaceholder()
						.withClassOnElements("btn-xs")
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
