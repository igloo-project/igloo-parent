package basicapp.front.navigation.page;

import basicapp.back.business.announcement.service.business.IAnnouncementService;
import basicapp.front.common.template.MainTemplate;
import basicapp.front.profile.page.ProfilePage;
import basicapp.front.referencedata.page.ReferenceDataPage;
import basicapp.front.user.page.BasicUserListPage;
import igloo.bootstrap.js.statement.JsLiteral;
import igloo.vuedatepicker.Component.DatePickerRangeVueField;
import igloo.vuedatepicker.Component.DatePickerVueField;
import igloo.vuedatepicker.Component.DateTimePickerVueField;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class HomePage extends MainTemplate {

  private static final long serialVersionUID = -6767518941118385548L;

  @SpringBean private IAnnouncementService announcementService;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(HomePage.class);
  }

  public HomePage(PageParameters parameters) {
    super(parameters);

    Form<Date> form = new Form<>("form");

    IModel<LocalDate> dateModel = Model.of(LocalDate.now());
    IModel<LocalDate> dateModel2 = Model.of(LocalDate.now());
    IModel<LocalDateTime> dateTimeModel = Model.of(LocalDateTime.now());

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("navigation.home"), HomePage.linkDescriptor()));

    DatePickerVueField datePicker1 = new DatePickerVueField("datePicker1", dateModel);
    DatePickerVueField datePicker2 =
        new DatePickerVueField(
            "datePicker2",
            dateModel2,
            DatePickerVueField.dateMinConsumer(datePicker1)
                .andThen(
                    builder ->
                        builder.vif(JsLiteral.of("%s".formatted(datePicker1.getVModelVarName())))));
    DatePickerRangeVueField dateRange =
        new DatePickerRangeVueField("dateRange", dateModel, dateModel2);
    add(
        form.add(
            datePicker1
                //                        .setRequired(true)
                .setOutputMarkupId(true),
            /*                .add(
            new UpdateOnChangeAjaxEventBehavior()
                .onChange(
                    new SerializableListener() {
                      private static final long serialVersionUID = 1L;

                      @Override
                      public void onBeforeRespond(
                          Map<String, Component> map, AjaxRequestTarget target) {
                          if (dateModel.getObject())
                        dateModel2.setObject(dateModel.getObject().plusDays(1));
                        target.add(datePicker2);
                      }
                    })),*/
            datePicker2
                .setRequired(true)
                .setOutputMarkupId(true)
                .add(
                    new UpdateOnChangeAjaxEventBehavior()
                        .onChange(
                            new SerializableListener() {
                              private static final long serialVersionUID = 1L;

                              @Override
                              public void onBeforeRespond(
                                  Map<String, Component> map, AjaxRequestTarget target) {
                                //
                                // target.add(dateRange);
                                target.add(datePicker1);
                              }
                            })),
            new DateTimePickerVueField("dateTimePicker", dateTimeModel),
            dateRange.add(new UpdateOnChangeAjaxEventBehavior()),
            new AjaxButton("save") {
              private static final long serialVersionUID = 1L;

              @Override
              protected void onSubmit(AjaxRequestTarget target) {
                try {
                  announcementService.test(
                      dateModel.getObject(), dateModel2.getObject(), dateTimeModel.getObject());
                  Session.get().success(getString("common.success"));
                } catch (RestartResponseException e) { // NOSONAR
                  throw e;
                } catch (Exception e) {
                  Session.get().error(getString("common.error.unexpected"));
                }
                FeedbackUtils.refreshFeedback(target, getPage());
              }

              @Override
              protected void onError(AjaxRequestTarget target) {
                FeedbackUtils.refreshFeedback(target, getPage());
              }
            }));

    add(
        BasicUserListPage.linkDescriptor().link("users").hideIfInvalid(),
        ReferenceDataPage.linkDescriptor().link("referenceData").hideIfInvalid(),
        ProfilePage.linkDescriptor().link("profile").hideIfInvalid());
  }

  @Override
  protected Condition displayBreadcrumb() {
    return Condition.alwaysFalse();
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return HomePage.class;
  }
}
