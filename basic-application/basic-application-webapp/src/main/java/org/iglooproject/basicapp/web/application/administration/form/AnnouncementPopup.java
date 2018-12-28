package org.iglooproject.basicapp.web.application.administration.form;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.basicapp.core.business.announcement.model.atomic.AnnouncementType;
import org.iglooproject.basicapp.core.business.announcement.service.IAnnouncementService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationAnnouncementListPage;
import org.iglooproject.basicapp.web.application.common.form.TimeField;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.form.DatePicker;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.FormMode;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.DelegatedMarkupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.autosize.AutosizeBehavior;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.util.DatePattern;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnouncementPopup extends AbstractAjaxModalPopupPanel<Announcement> {

	private static final long serialVersionUID = -757028645835449815L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementPopup.class);

	@SpringBean
	private IAnnouncementService announcementService;

	private Form<Announcement> form;

	private final IModel<FormMode> formModeModel = new Model<>(FormMode.ADD);

	private final IModel<AnnouncementType> typeModel = BindingModel.of(getModel(), Bindings.announcement().type());

	private final IModel<Date> interruptionStartDateModel = BindingModel.of(getModel(), Bindings.announcement().interruption().startDateTime());
	private final IModel<Date> interruptionStartTimeModel = Model.of();
	private final IModel<Date> interruptionEndDateModel = BindingModel.of(getModel(), Bindings.announcement().interruption().endDateTime());
	private final IModel<Date> interruptionEndTimeModel = Model.of();

	private final IModel<Date> publicationStartDateModel = BindingModel.of(getModel(), Bindings.announcement().publication().startDateTime());
	private final IModel<Date> publicationStartTimeModel = Model.of();
	private final IModel<Date> publicationEndDateModel = BindingModel.of(getModel(), Bindings.announcement().publication().endDateTime());
	private final IModel<Date> publicationEndTimeModel = Model.of();

	public AnnouncementPopup(String id) {
		super(id, new GenericEntityModel<Long, Announcement>(new Announcement()));
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
		
		Condition isTypeServiceInterruption = Condition.predicate(
			BindingModel.of(getModel(),Bindings.announcement().type()),
			Predicates2.isEqual(AnnouncementType.SERVICE_INTERRUPTION)
		);
		
		form = new Form<>("form", getModel());
		body.add(form);
		
		form.add(
			new EnumDropDownSingleChoice<>("type", typeModel, AnnouncementType.class)
				.setLabel(new ResourceModel("business.announcement.type"))
				.setRequired(true)
				.add(
					new UpdateOnChangeAjaxEventBehavior()
						.onChange(new SerializableListener() {
							private static final long serialVersionUID = 1L;
							@Override
							public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
								target.add(form);
							}
						})
				),
			new EnclosureContainer("interruptionContainer")
				.condition(isTypeServiceInterruption)
				.add(
					new CoreLabel("interruptionStartDateTitle", new ResourceModel("business.announcement.interruption.startDateTime")),
					new DatePicker("interruptionStartDate", interruptionStartDateModel, DatePattern.SHORT_DATE)
						.setLabel(new ResourceModel("business.announcement.interruption.startDateTime.date"))
						.setRequired(true)
						.add(new AttributeModifier("placeholder", new ResourceModel("date.format.shortDate.placeholder"))),
					new TimeField("interruptionStartTime", interruptionStartTimeModel, DatePattern.TIME)
						.setLabel(new ResourceModel("business.announcement.interruption.startDateTime.time"))
						.setRequired(true),
					new CoreLabel("interruptionEndDateTitle", new ResourceModel("business.announcement.interruption.endDateTime")),
					new DatePicker("interruptionEndDate", interruptionEndDateModel, DatePattern.SHORT_DATE)
						.setLabel(new ResourceModel("business.announcement.interruption.endDateTime.date"))
						.setRequired(true)
						.add(new AttributeModifier("placeholder", new ResourceModel("date.format.shortDate.placeholder"))),
					new TimeField("interruptionEndTime", interruptionEndTimeModel, DatePattern.TIME)
						.setLabel(new ResourceModel("business.announcement.interruption.endDateTime.time"))
						.setRequired(true)
				),
			new EnclosureContainer("descriptionContainer")
				.condition(isTypeServiceInterruption.negate())
				.add(
					new TextField<String>("titleFr", BindingModel.of(getModel(), Bindings.announcement().title().fr()))
						.setLabel(new ResourceModel("business.announcement.title.fr"))
						.setRequired(isTypeServiceInterruption.negate().applies()),
					new TextField<String>("titleEn", BindingModel.of(getModel(), Bindings.announcement().title().en()))
						.setLabel(new ResourceModel("business.announcement.title.en"))
						.setRequired(isTypeServiceInterruption.negate().applies()),
					new TextArea<String>("descriptionFr", BindingModel.of(getModel(), Bindings.announcement().description().fr()))
						.setLabel(new ResourceModel("business.announcement.description.fr"))
						.add(new AutosizeBehavior()),
					new TextArea<String>("descriptionEn", BindingModel.of(getModel(), Bindings.announcement().description().en()))
						.setLabel(new ResourceModel("business.announcement.description.en"))
						.add(new AutosizeBehavior())
				),
			new CoreLabel("publicationStartDateTitle", new ResourceModel("business.announcement.publication.startDateTime")),
			new DatePicker("publicationStartDate", publicationStartDateModel, DatePattern.SHORT_DATE)
				.setLabel(new ResourceModel("business.announcement.publication.startDateTime.date"))
				.setRequired(true)
				.add(new AttributeModifier("placeholder", new ResourceModel("date.format.shortDate.placeholder"))),
			new TimeField("publicationStartTime", publicationStartTimeModel, DatePattern.TIME)
				.setLabel(new ResourceModel("business.announcement.publication.startDateTime.time"))
				.setRequired(true),
			new CoreLabel("publicationEndDateTitle", new ResourceModel("business.announcement.publication.endDateTime")),
			new DatePicker("publicationEndDate", publicationEndDateModel, DatePattern.SHORT_DATE)
				.setLabel(new ResourceModel("business.announcement.publication.endDateTime.date"))
				.setRequired(true)
				.add(new AttributeModifier("placeholder", new ResourceModel("date.format.shortDate.placeholder"))),
			new TimeField("publicationEndTime", publicationEndTimeModel, DatePattern.TIME)
				.setLabel(new ResourceModel("business.announcement.publication.endDateTime.time"))
				.setRequired(true),
			new CheckBox("active", BindingModel.of(getModel(), Bindings.announcement().active()))
				.setLabel(new ResourceModel("business.announcement.active"))
				.setOutputMarkupId(true)
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
						announcement.getPublication().setStartDateTime(AnnouncementPopup.this.getpublicationDateTime(publicationStartDateModel, publicationStartTimeModel));
						announcement.getPublication().setEndDateTime(AnnouncementPopup.this.getpublicationDateTime(publicationEndDateModel, publicationEndTimeModel));
						announcement.getInterruption().setStartDateTime(AnnouncementPopup.this.getpublicationDateTime(interruptionStartDateModel, interruptionStartTimeModel));
						announcement.getInterruption().setEndDateTime(AnnouncementPopup.this.getpublicationDateTime(interruptionEndDateModel, interruptionEndTimeModel));
						
						if(FormMode.EDIT.equals(formModeModel.getObject())) {
							announcementService.update(announcement);
						} else {
							announcementService.create(announcement);
						}
						
						Session.get().success(getString("common.success"));
						throw AdministrationAnnouncementListPage.linkDescriptor().newRestartResponseException();
					} catch (RestartResponseException e) { // NOSONAR
						throw e;
					} catch (Exception e) {
						LOGGER.error("Erreur lors de la création d'une annonce.", e);
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

	private Date getpublicationDateTime(IModel<Date> publicationDate, IModel<Date> publicationTime) {
		if(publicationDate.getObject() == null || publicationTime.getObject() == null) {
			return null;
		}
		
		Calendar calTime = Calendar.getInstance();
		calTime.setTime(publicationTime.getObject());
		
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(publicationDate.getObject());
		
		calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
		calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
		
		return calDate.getTime();
	}

	@Override
	protected void onShow(AjaxRequestTarget target) {
		super.onShow(target);
		
		if (FormMode.ADD.equals(formModeModel.getObject())) {
			typeModel.setObject(AnnouncementType.SERVICE_INTERRUPTION);
			publicationStartTimeModel.setObject(null);
			publicationEndTimeModel.setObject(null);
			interruptionStartTimeModel.setObject(null);
			interruptionEndTimeModel.setObject(null);
		} else {
			publicationStartTimeModel.setObject(CloneUtils.clone(getModelObject().getPublication().getStartDateTime()));
			publicationEndTimeModel.setObject(CloneUtils.clone(getModelObject().getPublication().getEndDateTime()));
			
			if (AnnouncementType.SERVICE_INTERRUPTION.equals(getModelObject().getType())) {
				interruptionStartTimeModel.setObject(CloneUtils.clone(getModelObject().getInterruption().getStartDateTime()));
				interruptionEndTimeModel.setObject(CloneUtils.clone(getModelObject().getInterruption().getEndDateTime()));
			}
		}
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(
			formModeModel,
			typeModel,
			publicationEndDateModel,
			publicationEndTimeModel,
			publicationStartDateModel,
			publicationStartTimeModel,
			interruptionStartTimeModel,
			interruptionStartTimeModel,
			interruptionEndDateModel,
			interruptionEndTimeModel
		);
	}

	public void setUpAdd(Announcement announcement) {
		getModel().setObject(announcement);
		formModeModel.setObject(FormMode.ADD);
	}

	public void setUpEdit(Announcement announcement) {
		getModel().setObject(announcement);
		formModeModel.setObject(FormMode.EDIT);
	}

	protected Condition addModeCondition() {
		return FormMode.ADD.condition(formModeModel);
	}

	protected Condition editModeCondition() {
		return FormMode.EDIT.condition(formModeModel);
	}

}
