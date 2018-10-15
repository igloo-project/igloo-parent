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
import org.iglooproject.basicapp.core.business.message.model.GeneralMessage;
import org.iglooproject.basicapp.core.business.message.model.atomic.GeneralMessageType;
import org.iglooproject.basicapp.core.business.message.service.IGeneralMessageService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationGeneralMessageListPage;
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

public class GeneralMessagePopup extends AbstractAjaxModalPopupPanel<GeneralMessage> {

	private static final long serialVersionUID = -757028645835449815L;

	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralMessagePopup.class);

	@SpringBean
	private IGeneralMessageService generalMessageService;

	private Form<GeneralMessage> form;

	private final IModel<FormMode> formModeModel = new Model<>(FormMode.ADD);

	private IModel<GeneralMessageType> typeModel;

	private IModel<Date> publicationStartDateModel;
	private IModel<Date> publicationStartTimeModel = Model.of();
	private IModel<Date> publicationEndDateModel;
	private IModel<Date> publicationEndTimeModel = Model.of();

	private IModel<Date> interruptionStartDateModel;
	private IModel<Date> interruptionStartTimeModel = Model.of();
	private IModel<Date> interruptionEndDateModel;
	private IModel<Date> interruptionEndTimeModel = Model.of();

	public GeneralMessagePopup(String id) {
		super(id, new GenericEntityModel<Long, GeneralMessage>(new GeneralMessage()));
		
		typeModel = BindingModel.of(getModel(), Bindings.generalMessage().type());
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new CoreLabel(
			wicketId,
			addModeCondition()
				.then(new ResourceModel("administration.generalMessage.action.add.title"))
				.otherwise(new ResourceModel("administration.generalMessage.action.edit.title"))
		);
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());
		
		publicationStartDateModel = BindingModel.of(getModel(), Bindings.generalMessage().publication().startDateTime());
		publicationEndDateModel = BindingModel.of(getModel(), Bindings.generalMessage().publication().endDateTime());
		interruptionStartDateModel = BindingModel.of(getModel(), Bindings.generalMessage().interruption().startDateTime());
		interruptionEndDateModel = BindingModel.of(getModel(), Bindings.generalMessage().interruption().endDateTime());
		
		Condition isTypeInterruption = Condition.predicate(
			BindingModel.of(getModel(),Bindings.generalMessage().type()),
			Predicates2.isEqual(GeneralMessageType.SERVICE_INTERRUPTION)
		);
		
		form = new Form<>("form", getModel());
		
		body.add(form);
		
		TextField<String> titleFr = new TextField<String>("titleFr", BindingModel.of(getModel(), Bindings.generalMessage().title().fr()));
		TextField<String> titleEn = new TextField<String>("titleEn", BindingModel.of(getModel(), Bindings.generalMessage().title().en()));
		TextArea<String> descriptionFr = new TextArea<String>("descriptionFr", BindingModel.of(getModel(), Bindings.generalMessage().description().fr()));
		TextArea<String> descriptionEn = new TextArea<String>("descriptionEn", BindingModel.of(getModel(), Bindings.generalMessage().description().en()));
		
		form.add(
			new EnumDropDownSingleChoice<>("type", typeModel, GeneralMessageType.class)
				.setLabel(new ResourceModel("business.generalMessage.type"))
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
			new EnclosureContainer("descriptionContainer")
				.condition(isTypeInterruption.negate())
				.add(
					titleFr
						.setLabel(new ResourceModel("business.generalMessage.title.fr"))
						.setRequired(isTypeInterruption.negate().applies()),
					titleEn
						.setLabel(new ResourceModel("business.generalMessage.title.en"))
						.setRequired(isTypeInterruption.negate().applies()),
					descriptionFr
						.setLabel(new ResourceModel("business.generalMessage.description.fr"))
						.add(new AutosizeBehavior()),
					descriptionEn
						.setLabel(new ResourceModel("business.generalMessage.description.en"))
						.add(new AutosizeBehavior())
				),
			new CoreLabel("publicationStartDateTitle", new ResourceModel("business.generalMessage.publication.startDateTime")),
			new DatePicker("publicationStartDate", publicationStartDateModel, DatePattern.SHORT_DATE)
				.setLabel(new ResourceModel("business.generalMessage.publication.startDateTime.date"))
				.setRequired(true)
				.add(new AttributeModifier("placeholder", new ResourceModel("date.format.shortDate.placeholder"))),
			new TimeField("publicationStartTime", publicationStartTimeModel, DatePattern.TIME)
				.setLabel(new ResourceModel("business.generalMessage.publication.startDateTime.time"))
				.setRequired(true),
			new CoreLabel("publicationEndDateTitle", new ResourceModel("business.generalMessage.publication.endDateTime")),
			new DatePicker("publicationEndDate", publicationEndDateModel, DatePattern.SHORT_DATE)
				.setLabel(new ResourceModel("business.generalMessage.publication.endDateTime.date"))
				.setRequired(true)
				.add(new AttributeModifier("placeholder", new ResourceModel("date.format.shortDate.placeholder"))),
			new TimeField("publicationEndTime", publicationEndTimeModel, DatePattern.TIME)
				.setLabel(new ResourceModel("business.generalMessage.publication.endDateTime.time"))
				.setRequired(true),
			new EnclosureContainer("interruptionContainer")
				.condition(isTypeInterruption)
				.add(
					new CoreLabel("interruptionStartDateTitle", new ResourceModel("business.generalMessage.interruption.startDateTime")),
					new DatePicker("interruptionStartDate", interruptionStartDateModel, DatePattern.SHORT_DATE)
						.setLabel(new ResourceModel("business.generalMessage.interruption.startDateTime.date"))
						.setRequired(true)
						.add(new AttributeModifier("placeholder", new ResourceModel("date.format.shortDate.placeholder"))),
					new TimeField("interruptionStartTime", interruptionStartTimeModel, DatePattern.TIME)
						.setLabel(new ResourceModel("business.generalMessage.interruption.startDateTime.time"))
						.setRequired(true),
					new CoreLabel("interruptionEndDateTitle", new ResourceModel("business.generalMessage.interruption.endDateTime")),
					new DatePicker("interruptionEndDate", interruptionEndDateModel, DatePattern.SHORT_DATE)
						.setLabel(new ResourceModel("business.generalMessage.interruption.endDateTime.date"))
						.setRequired(true)
						.add(new AttributeModifier("placeholder", new ResourceModel("date.format.shortDate.placeholder"))),
					new TimeField("interruptionEndTime", interruptionEndTimeModel, DatePattern.TIME)
						.setLabel(new ResourceModel("business.generalMessage.interruption.endDateTime.time"))
						.setRequired(true)
				),
			new CheckBox("active", BindingModel.of(getModel(), Bindings.generalMessage().active()))
				.setLabel(new ResourceModel("business.generalMessage.active"))
				.setOutputMarkupId(true)
		);
		
		return body;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, getClass());
		
		footer.add(new AjaxButton("save", form) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				try {
					
					GeneralMessage generalMessage = GeneralMessagePopup.this.getModelObject();
					generalMessage.getPublication().setStartDateTime(GeneralMessagePopup.this.getpublicationDateTime(publicationStartDateModel, publicationStartTimeModel));
					generalMessage.getPublication().setEndDateTime(GeneralMessagePopup.this.getpublicationDateTime(publicationEndDateModel, publicationEndTimeModel));
					generalMessage.getInterruption().setStartDateTime(GeneralMessagePopup.this.getpublicationDateTime(interruptionStartDateModel, interruptionStartTimeModel));
					generalMessage.getInterruption().setEndDateTime(GeneralMessagePopup.this.getpublicationDateTime(interruptionEndDateModel, interruptionEndTimeModel));
					
					if(FormMode.EDIT.equals(formModeModel.getObject())) {
						generalMessageService.update(generalMessage);
					} else {
						generalMessageService.create(generalMessage);
					}
					
					Session.get().success(getString("common.success"));
					throw AdministrationGeneralMessageListPage.linkDescriptor().newRestartResponseException();
				} catch (RestartResponseException e) { // NOSONAR
					throw e;
				} catch (Exception e) {
					LOGGER.error("Erreur lors de la création d'un message de service.", e);
					Session.get().error(getString("common.error.unexpected"));
				}
				
				FeedbackUtils.refreshFeedback(target, getPage());
			}
			
			@Override
			protected void onError(AjaxRequestTarget target) {
				FeedbackUtils.refreshFeedback(target, getPage());
			}
		});
		
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
			typeModel.setObject(GeneralMessageType.SERVICE_INTERRUPTION);
			publicationStartTimeModel.setObject(null);
			publicationEndTimeModel.setObject(null);
			interruptionStartTimeModel.setObject(null);
			interruptionEndTimeModel.setObject(null);
		} else {
			publicationStartTimeModel.setObject(CloneUtils.clone(getModelObject().getPublication().getStartDateTime()));
			publicationEndTimeModel.setObject(CloneUtils.clone(getModelObject().getPublication().getEndDateTime()));
			
			if (GeneralMessageType.SERVICE_INTERRUPTION.equals(getModelObject().getType())) {
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

	public void setUpAdd(GeneralMessage generalMessage) {
		getModel().setObject(generalMessage);
		formModeModel.setObject(FormMode.ADD);
	}

	public void setUpEdit(GeneralMessage generalMessage) {
		getModel().setObject(generalMessage);
		formModeModel.setObject(FormMode.EDIT);
	}

	protected Condition addModeCondition() {
		return FormMode.ADD.condition(formModeModel);
	}

	protected Condition editModeCondition() {
		return FormMode.EDIT.condition(formModeModel);
	}

}
