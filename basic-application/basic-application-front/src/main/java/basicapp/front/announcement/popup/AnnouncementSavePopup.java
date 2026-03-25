package basicapp.front.announcement.popup;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.model.atomic.AnnouncementType;
import basicapp.back.business.announcement.predicate.AnnouncementPredicates;
import basicapp.back.business.announcement.service.controller.IAnnouncementControllerService;
import basicapp.back.util.binding.Bindings;
import basicapp.front.announcement.model.AnnouncementBindableModel;
import basicapp.front.announcement.page.AnnouncementListPage;
import igloo.bootstrap.modal.AbstractAjaxModalPopupPanel;
import igloo.vuedatepicker.component.LocalDateTimeRangePickerVueField;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.Detachables;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.bindable.form.CacheWritingForm;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.FormMode;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnouncementSavePopup extends AbstractAjaxModalPopupPanel<Announcement> {

  private static final long serialVersionUID = -757028645835449815L;

  private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementSavePopup.class);

  @SpringBean private IAnnouncementControllerService announcementControllerService;

  private final IModel<FormMode> formModeModel = Model.of();

  private final AnnouncementBindableModel bindableModel;

  private Form<Announcement> form;

  public AnnouncementSavePopup(String id) {
    this(id, new AnnouncementBindableModel());
  }

  public AnnouncementSavePopup(String id, AnnouncementBindableModel bindableModel) {
    super(id, bindableModel);
    this.bindableModel = bindableModel;
  }

  @Override
  protected Component createHeader(String wicketId) {
    return new CoreLabel(
        wicketId,
        addModeCondition()
            .then(new ResourceModel("announcement.add.title"))
            .otherwise(new ResourceModel("announcement.edit.title")));
  }

  @Override
  protected Component createBody(String wicketId) {
    DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());

    form = new CacheWritingForm<>("form", bindableModel);
    body.add(form);

    WebMarkupContainer formContainer = new WebMarkupContainer("formContainer");
    form.add(formContainer.setOutputMarkupId(true));

    formContainer.add(
        new EnumDropDownSingleChoice<>(
                "type", bindableModel.bind(Bindings.announcement().type()), AnnouncementType.class)
            .setLabel(new ResourceModel("business.announcement.type"))
            .setRequired(true)
            .add(
                new UpdateOnChangeAjaxEventBehavior()
                    .onChange(
                        new SerializableListener() {
                          private static final long serialVersionUID = 1L;

                          @Override
                          public void onBeforeRespond(
                              Map<String, Component> map, AjaxRequestTarget target) {
                            bindableModel.writeAll();
                            Announcement announcement = getModelObject();
                            announcementControllerService.cleanWithoutSaving(announcement);
                            bindableModel.readAll();
                            target.add(formContainer);
                          }
                        })),
        new EnclosureContainer("notificationContainer")
            .condition(
                Condition.predicate(
                    getModel(), AnnouncementPredicates.type(AnnouncementType.NOTIFICATION)))
            .add(
                new TextArea<>(
                        "contentFr", bindableModel.bind(Bindings.announcement().content().fr()))
                    .setLabel(new ResourceModel("business.announcement.content.fr"))
                    .setRequired(true)
                    .add(new UpdateOnChangeAjaxEventBehavior()),
                new TextArea<>(
                        "contentEn", bindableModel.bind(Bindings.announcement().content().en()))
                    .setLabel(new ResourceModel("business.announcement.content.en"))
                    .setRequired(true)
                    .add(new UpdateOnChangeAjaxEventBehavior())),
        new EnclosureContainer("unavailabilityContainer")
            .condition(
                Condition.predicate(
                    getModel(), AnnouncementPredicates.type(AnnouncementType.UNAVAILABILITY)))
            .add(
                new CoreLabel(
                    "unavailabilityDatesLabel",
                    new ResourceModel("business.announcement.unavailability.dates")),
                new LocalDateTimeRangePickerVueField(
                        "unavailabilityDates",
                        bindableModel.bind(
                            Bindings.announcement().unavailability().startDateTime()),
                        bindableModel.bind(Bindings.announcement().unavailability().endDateTime()))
                    .setRequired(true)
                    .add(new UpdateOnChangeAjaxEventBehavior())),
        new CoreLabel(
            "publicationDateTitle", new ResourceModel("business.announcement.publication.dates")),
        new LocalDateTimeRangePickerVueField(
                "publicationDate",
                bindableModel.bind(Bindings.announcement().publication().startDateTime()),
                bindableModel.bind(Bindings.announcement().publication().endDateTime()))
            .setRequired(true)
            .add(new UpdateOnChangeAjaxEventBehavior()),
        new CheckBox("enabled", bindableModel.bind(Bindings.announcement().enabled()))
            .setLabel(new ResourceModel("business.announcement.enabled"))
            .setOutputMarkupId(true)
            .add(new UpdateOnChangeAjaxEventBehavior()));

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
              bindableModel.writeAll();
              Announcement announcement = bindableModel.getObject();

              announcementControllerService.saveAnnouncement(announcement);

              Session.get().success(getString("common.success"));
              throw AnnouncementListPage.linkDescriptor().newRestartResponseException();
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
        });

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
    formModeModel.setObject(FormMode.ADD);
    getModel().setObject(announcement);
  }

  public void setUpEdit(Announcement announcement) {
    formModeModel.setObject(FormMode.EDIT);
    getModel().setObject(announcement);
  }

  private Condition addModeCondition() {
    return FormMode.ADD.condition(formModeModel);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(formModeModel, bindableModel);
  }
}
