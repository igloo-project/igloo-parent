package org.iglooproject.basicapp.web.application.administration.form;

import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.basicapp.core.business.announcement.model.atomic.AnnouncementType;
import org.iglooproject.basicapp.core.business.announcement.predicate.AnnouncementPredicates;
import org.iglooproject.basicapp.core.business.announcement.service.IAnnouncementService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.model.AnnouncementBindableModel;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationAnnouncementListPage;
import org.iglooproject.basicapp.web.application.common.form.TimeField;
import org.iglooproject.basicapp.web.application.common.util.Masks;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.bindable.form.CacheWritingForm;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.form.DatePicker;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.DelegatedMarkupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.mask.MaskBehavior;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.util.DatePattern;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnouncementPopup extends AbstractAjaxModalPopupPanel<Announcement> {

	private static final long serialVersionUID = -757028645835449815L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementPopup.class);

	@SpringBean
	private IAnnouncementService announcementService;

	private final AnnouncementBindableModel bindableModel;

	private Form<Announcement> form;

	public AnnouncementPopup(String id) {
		this(id, new AnnouncementBindableModel());
	}

	public AnnouncementPopup(String id, AnnouncementBindableModel bindableModel) {
		super(id, bindableModel);
		this.bindableModel = bindableModel;
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new CoreLabel(
			wicketId,
			addModeCondition()
				.then(new ResourceModel("administration.announcement.action.add.title"))
				.otherwise(new ResourceModel("administration.announcement.action.edit.title"))
		);
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());
		CoreLabel interruptionStartDateTitle = new CoreLabel("interruptionStartDateTitle", new ResourceModel("business.announcement.interruption.startDateTime"));
		CoreLabel interruptionEndDateTitle = new CoreLabel("interruptionEndDateTitle", new ResourceModel("business.announcement.interruption.endDateTime"));
		CoreLabel publicationStartDateTitle = new CoreLabel("publicationStartDateTitle", new ResourceModel("business.announcement.publication.startDateTime"));
		CoreLabel publicationEndDateTitle = new CoreLabel("publicationEndDateTitle", new ResourceModel("business.announcement.publication.endDateTime"));
		
		Condition typeServiceInterruptionCondition = Condition.predicate(getModel(), AnnouncementPredicates.type(AnnouncementType.SERVICE_INTERRUPTION));
		
		form = new CacheWritingForm<>("form", bindableModel);
		body.add(form);
		
		WebMarkupContainer formContainer = new WebMarkupContainer("formContainer");
		form.add(
			formContainer
				.setOutputMarkupId(true)
		);
		
		formContainer.add(
			new EnumDropDownSingleChoice<>("type", bindableModel.bind(Bindings.announcement().type()), AnnouncementType.class)
				.setLabel(new ResourceModel("business.announcement.type"))
				.setRequired(true)
				.add(
					new UpdateOnChangeAjaxEventBehavior()
						.onChange(new SerializableListener() {
							private static final long serialVersionUID = 1L;
							@Override
							public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
								bindableModel.writeAll();
								Announcement announcement = getModelObject();
								announcementService.cleanWithoutSaving(announcement);
								bindableModel.readAll();
								target.add(formContainer);
							}
						})
				),
			new EnclosureContainer("interruptionContainer")
				.condition(typeServiceInterruptionCondition)
				.add(
					interruptionStartDateTitle
						.setOutputMarkupId(true),
					new DatePicker("interruptionStartDate", bindableModel.bind(Bindings.announcement().interruption().startDateTime()), DatePattern.SHORT_DATE)
						.setLabel(new ResourceModel("business.announcement.interruption.startDateTime.date"))
						.setRequired(true)
						.add(new MaskBehavior(Masks.DATE, Masks.dateOptions()))
						.add(new UpdateOnChangeAjaxEventBehavior())
						.add(
							new AttributeModifier("aria-labelledby", interruptionStartDateTitle::getMarkupId)
						),
					new TimeField("interruptionStartTime", bindableModel.getInterruptionStartTimeModel(), DatePattern.TIME)
						.setLabel(new ResourceModel("business.announcement.interruption.startDateTime.time"))
						.setRequired(true)
						.add(new UpdateOnChangeAjaxEventBehavior())
						.add(
								new AttributeModifier("aria-labelledby", interruptionStartDateTitle::getMarkupId)
						),
					interruptionEndDateTitle
						.setOutputMarkupId(true),
						new DatePicker("interruptionEndDate", bindableModel.bind(Bindings.announcement().interruption().endDateTime()), DatePattern.SHORT_DATE)
						.setLabel(new ResourceModel("business.announcement.interruption.endDateTime.date"))
						.setRequired(true)
						.add(new MaskBehavior(Masks.DATE, Masks.dateOptions()))
						.add(new UpdateOnChangeAjaxEventBehavior())
						.add(
							new AttributeModifier("aria-labelledby", interruptionEndDateTitle::getMarkupId)
						),
					new TimeField("interruptionEndTime", bindableModel.getInterruptionEndTimeModel(), DatePattern.TIME)
						.setLabel(new ResourceModel("business.announcement.interruption.endDateTime.time"))
						.setRequired(true)
						.add(new UpdateOnChangeAjaxEventBehavior())
						.add(
							new AttributeModifier("aria-labelledby", interruptionEndDateTitle::getMarkupId)
						)
				),
			new EnclosureContainer("descriptionContainer")
				.condition(typeServiceInterruptionCondition.negate())
				.add(
					new TextField<String>("titleFr", bindableModel.bind(Bindings.announcement().title().fr()))
						.setLabel(new ResourceModel("business.announcement.title.fr"))
						.setRequired(typeServiceInterruptionCondition.negate().applies())
						.add(new UpdateOnChangeAjaxEventBehavior()),
					new TextField<String>("titleEn", bindableModel.bind(Bindings.announcement().title().en()))
						.setLabel(new ResourceModel("business.announcement.title.en"))
						.setRequired(typeServiceInterruptionCondition.negate().applies())
						.add(new UpdateOnChangeAjaxEventBehavior()),
					new TextArea<String>("descriptionFr", bindableModel.bind(Bindings.announcement().description().fr()))
						.setLabel(new ResourceModel("business.announcement.description.fr"))
						.add(new UpdateOnChangeAjaxEventBehavior()),
					new TextArea<String>("descriptionEn", bindableModel.bind(Bindings.announcement().description().en()))
						.setLabel(new ResourceModel("business.announcement.description.en"))
						.add(new UpdateOnChangeAjaxEventBehavior())
				),
			publicationStartDateTitle
				.setOutputMarkupId(true),
			new DatePicker("publicationStartDate", bindableModel.bind(Bindings.announcement().publication().startDateTime()), DatePattern.SHORT_DATE)
				.setLabel(new ResourceModel("business.announcement.publication.startDateTime.date"))
				.setRequired(true)
				.add(new MaskBehavior(Masks.DATE, Masks.dateOptions()))
				.add(new UpdateOnChangeAjaxEventBehavior())
				.add(
					new AttributeModifier("aria-labelledby", publicationStartDateTitle::getMarkupId)
				),
			new TimeField("publicationStartTime", bindableModel.getPublicationStartTimeModel(), DatePattern.TIME)
				.setLabel(new ResourceModel("business.announcement.publication.startDateTime.time"))
				.setRequired(true)
				.add(new UpdateOnChangeAjaxEventBehavior())
				.add(
					new AttributeModifier("aria-labelledby", publicationStartDateTitle::getMarkupId)
				),
				
			publicationEndDateTitle
				.setOutputMarkupId(true),
			new DatePicker("publicationEndDate", bindableModel.bind(Bindings.announcement().publication().endDateTime()), DatePattern.SHORT_DATE)
				.setLabel(new ResourceModel("business.announcement.publication.endDateTime.date"))
				.setRequired(true)
				.add(new MaskBehavior(Masks.DATE, Masks.dateOptions()))
				.add(new UpdateOnChangeAjaxEventBehavior())
				.add(
					new AttributeModifier("aria-labelledby", publicationEndDateTitle::getMarkupId)
				),
			new TimeField("publicationEndTime", bindableModel.getPublicationEndTimeModel(), DatePattern.TIME)
				.setLabel(new ResourceModel("business.announcement.publication.endDateTime.time"))
				.setRequired(true)
				.add(new UpdateOnChangeAjaxEventBehavior())
				.add(
					new AttributeModifier("aria-labelledby", publicationEndDateTitle::getMarkupId)
				),
			new CheckBox("active", bindableModel.bind(Bindings.announcement().active()))
				.setLabel(new ResourceModel("business.announcement.active"))
				.setOutputMarkupId(true)
				.add(new UpdateOnChangeAjaxEventBehavior())
		);
		
		return body;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, getClass());
		
		footer.add(
			new AjaxButton("save", form) {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					try {
						Announcement announcement = AnnouncementPopup.this.getModelObject();
						
						announcementService.saveAnnouncement(announcement);
						
						Session.get().success(getString("common.success"));
						throw AdministrationAnnouncementListPage.linkDescriptor().newRestartResponseException();
					} catch (RestartResponseException e) { // NOSONAR
						throw e;
					} catch (Exception e) {
						LOGGER.error("An error occured while saving an announcement.", e);
						Session.get().error(getString("common.error.unexpected"));
					}
					FeedbackUtils.refreshFeedback(target, getPage());
				}
				
				@Override
				protected void onError(AjaxRequestTarget target) {
					FeedbackUtils.refreshFeedback(target, getPage());
				}
			}
		);
		
		BlankLink cancel = new BlankLink("cancel");
		addCancelBehavior(cancel);
		footer.add(cancel);
		
		return footer;
	}

	@Override
	protected void onShow(AjaxRequestTarget target) {
		super.onShow(target);
		bindableModel.readAll();
	}

	public void setUpAdd(Announcement announcement) {
		getModel().setObject(announcement);
	}

	public void setUpEdit(Announcement announcement) {
		getModel().setObject(announcement);
	}

	protected Condition addModeCondition() {
		return Condition.isTrueOrNull(BindingModel.of(getModel(), Bindings.announcement().isNew()));
	}

	protected Condition editModeCondition() {
		return addModeCondition().negate();
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(bindableModel);
	}

}
