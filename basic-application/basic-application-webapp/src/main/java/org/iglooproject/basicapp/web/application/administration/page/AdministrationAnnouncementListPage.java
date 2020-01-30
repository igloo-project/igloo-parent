package org.iglooproject.basicapp.web.application.administration.page;

import static org.iglooproject.basicapp.web.application.common.util.CssClassConstants.TABLE_ROW_DISABLED;
import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.basicapp.core.business.announcement.predicate.AnnouncementPredicates;
import org.iglooproject.basicapp.core.business.announcement.search.AnnouncementSort;
import org.iglooproject.basicapp.core.business.announcement.service.IAnnouncementService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.form.AnnouncementPopup;
import org.iglooproject.basicapp.web.application.administration.model.AnnouncementDataProvider;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationAnnouncementTemplate;
import org.iglooproject.basicapp.web.application.common.component.AnnouncementMessagePanel;
import org.iglooproject.basicapp.web.application.common.renderer.ActionRenderers;
import org.iglooproject.basicapp.web.application.common.renderer.AnnouncementActiveRenderer;
import org.iglooproject.basicapp.web.application.common.util.CssClassConstants;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.action.IOneParameterAjaxAction;
import org.iglooproject.wicket.more.markup.html.action.OneParameterModalOpenAjaxAction;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import org.iglooproject.wicket.more.util.DatePattern;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class AdministrationAnnouncementListPage extends AdministrationAnnouncementTemplate {

	private static final long serialVersionUID = -4746355629579854697L;

	private Logger LOGGER = LoggerFactory.getLogger(AdministrationAnnouncementListPage.class);

	@SpringBean
	private IAnnouncementService announcementService;

	@SpringBean
	private IPropertyService propertyService;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
			.page(AdministrationAnnouncementListPage.class);
	}

	private final AnnouncementDataProvider dataProvider = new AnnouncementDataProvider();

	public AdministrationAnnouncementListPage(PageParameters parameters) {
		super(parameters);
		
		AnnouncementPopup popup = new AnnouncementPopup("popup");
		add(popup);
		
		EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
		add(headerElementsSection.anyChildVisible());
		
		headerElementsSection
			.add(
				new EnclosureContainer("actionsContainer")
					.anyChildVisible()
					.add(
						new BlankLink("add")
							.add(
								new AjaxModalOpenBehavior(popup, MouseEvent.CLICK) {
									private static final long serialVersionUID = 1L;
									@Override
									protected void onShow(AjaxRequestTarget target) {
										popup.setUpAdd(new Announcement());
									}
								}
							)
					)
			);
		
		DecoratedCoreDataTablePanel<?, ?> results =
			DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
				.addBootstrapBadgeColumn(Model.of(), Bindings.announcement(), AnnouncementActiveRenderer.get())
					.hideLabel()
					.withClass("narrow")
				.addLabelColumn(new ResourceModel("business.announcement.id"), Bindings.announcement().id())
					.withSort(AnnouncementSort.ID, SortIconStyle.NUMERIC, CycleMode.DEFAULT_REVERSE)
					.withClass("numeric numeric-sm")
				.addLabelColumn(new ResourceModel("business.announcement.type"), Bindings.announcement().type(), EnumRenderer.get())
				.addColumn(new AbstractCoreColumn<Announcement, AnnouncementSort>(new ResourceModel("business.announcement.message")) {
					private static final long serialVersionUID = 1L;
					@Override
					public void populateItem(Item<ICellPopulator<Announcement>> cellItem, String componentId, IModel<Announcement> rowModel) {
						cellItem.add(new AnnouncementMessagePanel(componentId, rowModel));
					}
				})
				.addLabelColumn(new ResourceModel("business.announcement.publication.startDateTime"), Bindings.announcement().publication().startDateTime(), DatePattern.REALLY_SHORT_DATETIME)
					.withSort(AnnouncementSort.PUBLICATION_START_DATE_TIME, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
					.withClass("date date-md")
				.addLabelColumn(new ResourceModel("business.announcement.publication.endDateTime"), Bindings.announcement().publication().endDateTime(), DatePattern.REALLY_SHORT_DATETIME)
					.withClass("date date-md")
				.addActionColumn()
					.addAction(ActionRenderers.edit(), new OneParameterModalOpenAjaxAction<IModel<Announcement>>(popup) {
						private static final long serialVersionUID = 1L;
						@Override
						protected void onShow(AjaxRequestTarget target, IModel<Announcement> announcementModel) {
							super.onShow(target, announcementModel);
							popup.setUpEdit(announcementModel.getObject());
						}
					})
					.addConfirmAction(ActionRenderers.delete())
						.title(new ResourceModel("administration.announcement.action.delete.confirmation.title"))
						.content(new ResourceModel("administration.announcement.action.delete.confirmation.content"))
						.confirm()
						.onClick(new IOneParameterAjaxAction<IModel<Announcement>>() {
							private static final long serialVersionUID = 1L;
							@Override
							public void execute(AjaxRequestTarget target, IModel<Announcement> parameter) {
								try {
									announcementService.delete(parameter.getObject());
									Session.get().success(getString("common.success"));
									throw new RestartResponseException(getPage());
								} catch (RestartResponseException e) {
									throw e;
								} catch (Exception e) {
									LOGGER.error("Error when deleting an announcement.", e);
									Session.get().error(getString("common.error.unexpected"));
									FeedbackUtils.refreshFeedback(target, getPage());
								}
							}
						})
					.withClassOnElements(CssClassConstants.BTN_TABLE_ROW_ACTION)
					.end()
					.withClass("actions actions-2x")
				.rows()
					.withClass(itemModel -> Condition.predicate(itemModel, AnnouncementPredicates.inactive()).then(TABLE_ROW_DISABLED).otherwise(""))
					.end()
				.bootstrapCard()
					.count("administration.announcement.list.count")
					.ajaxPagers()
				.build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));
		
		add(results);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(dataProvider);
	}

}
