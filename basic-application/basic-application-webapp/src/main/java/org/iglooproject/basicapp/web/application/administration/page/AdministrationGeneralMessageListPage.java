package org.iglooproject.basicapp.web.application.administration.page;

import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.message.model.GeneralMessage;
import org.iglooproject.basicapp.core.business.message.search.GeneralMessageSort;
import org.iglooproject.basicapp.core.business.message.service.IGeneralMessageService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.form.GeneralMessagePopup;
import org.iglooproject.basicapp.web.application.administration.model.GeneralMessageDataProvider;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationGeneralMessageTemplate;
import org.iglooproject.basicapp.web.application.common.renderer.ActionRenderers;
import org.iglooproject.basicapp.web.application.common.renderer.GeneralMessageActiveRenderer;
import org.iglooproject.basicapp.web.application.common.renderer.GeneralMessageRenderer;
import org.iglooproject.basicapp.web.application.common.util.CssClassConstants;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.action.IOneParameterAjaxAction;
import org.iglooproject.wicket.more.markup.html.action.OneParameterModalOpenAjaxAction;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.factory.IDetachableFactory;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import org.iglooproject.wicket.more.util.DatePattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class AdministrationGeneralMessageListPage extends AdministrationGeneralMessageTemplate {

	private static final long serialVersionUID = -4746355629579854697L;

	private Logger LOGGER = LoggerFactory.getLogger(AdministrationGeneralMessageListPage.class);

	@SpringBean
	private IGeneralMessageService generalMessageService;

	@SpringBean
	private IPropertyService propertyService;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(AdministrationGeneralMessageListPage.class);
	}

	public AdministrationGeneralMessageListPage(PageParameters parameters) {
		super(parameters);
		
		GeneralMessagePopup popup = new GeneralMessagePopup("popup");
		add(popup);
		
		EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
		add(headerElementsSection);
		
		headerElementsSection
			.anyChildVisible()
			.add(
				new BlankLink("add")
				.add(
					new AjaxModalOpenBehavior(popup, MouseEvent.CLICK) {
						private static final long serialVersionUID = 1L;
						@Override
						protected void onShow(AjaxRequestTarget target) {
							popup.setUpAdd(new GeneralMessage());
						}
					}
				)
			);
		
		GeneralMessageDataProvider dataProvider = new GeneralMessageDataProvider();
		
		DecoratedCoreDataTablePanel<?, ?> results =
		DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
			.addBootstrapBadgeColumn(Model.of(), Bindings.generalMessage(), GeneralMessageActiveRenderer.get())
				.hideLabel()
				.withClass("narrow")
			.addLabelColumn(new ResourceModel("administration.generalMessage.list.id"), Bindings.generalMessage().id())
				.withSort(GeneralMessageSort.ID, SortIconStyle.NUMERIC, CycleMode.DEFAULT_REVERSE)
				.withClass("numeric numeric-sm")
			.addLabelColumn(new ResourceModel("administration.generalMessage.list.type"), Bindings.generalMessage().type(), EnumRenderer.get())
			.addLabelColumn(new ResourceModel("administration.generalMessage.list.message"), GeneralMessageRenderer.get())
			.addLabelColumn(new ResourceModel("administration.generalMessage.list.publication.startDateTime"), Bindings.generalMessage().publication().startDateTime(), DatePattern.REALLY_SHORT_DATETIME)
				.withSort(GeneralMessageSort.PUBLICATION_START_DATE_TIME, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
				.withClass("date date-md")
			.addLabelColumn(new ResourceModel("administration.generalMessage.list.publication.endDateTime"), Bindings.generalMessage().publication().endDateTime(), DatePattern.REALLY_SHORT_DATETIME)
				.withClass("date date-md")
			.addActionColumn()
				.addAction(ActionRenderers.edit(), new OneParameterModalOpenAjaxAction<IModel<GeneralMessage>>(popup) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onShow(AjaxRequestTarget target, IModel<GeneralMessage> generalMessageModel) {
						super.onShow(target, generalMessageModel);
						popup.setUpEdit(generalMessageModel.getObject());
					}
				})
				.addConfirmAction(ActionRenderers.delete())
					.title(new IDetachableFactory<IModel<GeneralMessage>, IModel<String>>() {
						private static final long serialVersionUID = 1L;
						@Override
						public IModel<String> create(IModel<GeneralMessage> parameter) {
							return new ResourceModel("administration.generalMessage.action.delete.confirmation.title");
						}
					})
					.content(new ResourceModel("administration.generalMessage.action.delete.confirmation.content"))
					.confirm()
					.onClick(new IOneParameterAjaxAction<IModel<GeneralMessage>>() {
						private static final long serialVersionUID = 1L;
						@Override
						public void execute(AjaxRequestTarget target, IModel<GeneralMessage> parameter) {
							try {
								generalMessageService.delete(parameter.getObject());
								Session.get().success(getString("common.success"));
								throw new RestartResponseException(getPage());
							} catch (RestartResponseException e) {
								throw e;
							} catch (Exception e) {
								LOGGER.error("Erreur lors de la suppression d'un message de service", e);
								Session.get().error(getString("common.error.unexpected"));
								FeedbackUtils.refreshFeedback(target, getPage());
							}
						}
					})
					.withClassOnElements(CssClassConstants.BTN_TABLE_ROW_ACTION)
				.end()
				.withClass("actions actions-3x")
			.addRowCssClass(message -> (message != null && !message.isActive()) ? CssClassConstants.ROW_DISABLED : null)
			.bootstrapCard()
				.ajaxPagers()
			.build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));
		
		add(results);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}

}
