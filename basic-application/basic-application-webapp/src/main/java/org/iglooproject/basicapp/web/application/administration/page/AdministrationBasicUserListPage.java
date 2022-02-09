package org.iglooproject.basicapp.web.application.administration.page;

import static org.iglooproject.basicapp.web.application.common.util.CssClassConstants.CELL_DISPLAY_2XL;
import static org.iglooproject.basicapp.web.application.common.util.CssClassConstants.TABLE_ROW_DISABLED;
import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

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
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.predicate.UserPredicates;
import org.iglooproject.basicapp.core.business.user.search.UserSort;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.component.BasicUserListSearchPanel;
import org.iglooproject.basicapp.web.application.administration.export.UserExcelTableExport;
import org.iglooproject.basicapp.web.application.administration.form.BasicUserPopup;
import org.iglooproject.basicapp.web.application.administration.model.BasicUserDataProvider;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserListTemplate;
import org.iglooproject.basicapp.web.application.common.renderer.UserEnabledRenderer;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.api.bindgen.BindingModel;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.bootstrap5.markup.html.bootstrap.component.exportexcel.AbstractExcelExportAjaxLink;
import org.iglooproject.wicket.bootstrap5.markup.html.bootstrap.component.exportexcel.ExcelExportWorkInProgressModalPopupPanel;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.markup.html.link.EmailLink;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class AdministrationBasicUserListPage extends AdministrationUserListTemplate<BasicUser> {

	private static final long serialVersionUID = 8009837891166387406L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
			.page(AdministrationBasicUserListPage.class);
	}

	@SpringBean
	private IUserService userService;

	@SpringBean
	private IPropertyService propertyService;

	public AdministrationBasicUserListPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(
			new ResourceModel("navigation.administration.user.basicUser")
		));
		
		BasicUserDataProvider dataProvider = new BasicUserDataProvider();
		
		BasicUserPopup addPopup = new BasicUserPopup("addPopup");
		add(addPopup);
		
		ExcelExportWorkInProgressModalPopupPanel loadingPopup = new ExcelExportWorkInProgressModalPopupPanel("loadingPopup");
		add(loadingPopup);
		
		EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
		add(headerElementsSection.anyChildVisible());
		
		headerElementsSection
			.add(
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
								}
							)
					)
			);
		
		DecoratedCoreDataTablePanel<BasicUser, ?> results = DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
			.addBootstrapBadgeColumn(Model.of(), Bindings.user(), UserEnabledRenderer.get())
				.badgePill()
				.withClass("cell-w-100 text-center")
			.addLabelColumn(new ResourceModel("business.user.username"), Bindings.user().username())
				.withLink(AdministrationBasicUserDetailPage.MAPPER.setParameter2(new PageModel<>(this)))
				.withClass("cell-w-250")
			.addLabelColumn(new ResourceModel("business.user.lastName"), Bindings.user().lastName())
				.withSort(UserSort.LAST_NAME, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
				.withClass("cell-w-250")
			.addLabelColumn(new ResourceModel("business.user.firstName"), Bindings.user().firstName())
				.withSort(UserSort.FIRST_NAME, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
				.withClass("cell-w-250")
			.addColumn(new AbstractCoreColumn<BasicUser, UserSort>(new ResourceModel("business.user.email")) {
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item<ICellPopulator<BasicUser>> cellItem, String componentId, IModel<BasicUser> rowModel) {
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
				.withClass("cell-w-350")
				.withClass(CELL_DISPLAY_2XL)
			.rows()
				.withClass(itemModel -> Condition.predicate(itemModel, UserPredicates.disabled()).then(TABLE_ROW_DISABLED).otherwise(""))
				.end()
			.bootstrapCard()
				.ajaxPagers()
				.count("administration.user.list.count")
			.build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));
		
		add(
			new BasicUserListSearchPanel("search", results, dataProvider),
			results
		);
	}

}
