package basicapp.front.navigation.page;

import basicapp.back.business.announcement.service.business.IAnnouncementService;
import basicapp.front.common.template.MainTemplate;
import basicapp.front.profile.page.ProfilePage;
import basicapp.front.referencedata.page.ReferenceDataPage;
import basicapp.front.user.page.BasicUserListPage;
import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.Component.DatePickerRangeVueComponent;
import igloo.vuedatepicker.Component.DatePickerVueComponent;
import igloo.vuedatepicker.Component.DateTimePickerVueComponent;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.model.Detachables;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
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

  private final IModel<LocalDate> dateModel = Model.of(LocalDate.now());
  private final IModel<LocalDate> dateModel2 = Model.of(LocalDate.now());
  private final IModel<LocalDateTime> dateTimeModel = Model.of(LocalDateTime.now());

  public HomePage(PageParameters parameters) {
    super(parameters);

    Form<Date> form = new Form<>("form");

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("navigation.home"), HomePage.linkDescriptor()));
    //    TextField<Date> labelDate = new TextField<>("labelDate", dateModel);
    /*    DatePickerRangeVueComponent datePicker2 =
    new DatePickerRangeVueComponent("datePicker2", dateModel, dateModel2);*/
    DatePickerVueComponent datePicker1 = new DatePickerVueComponent("datePicker1", dateModel);
    DatePickerVueComponent datePicker2 =
        new DatePickerVueComponent(
            "datePicker2",
            dateModel2,
            builder -> builder.minDate(JsHelpers.ofLiteral(datePicker1.getVModelVarName())));
    DatePickerRangeVueComponent dateRange =
        new DatePickerRangeVueComponent("dateRange", dateModel, dateModel2);
    add(
        form.add(
            //            labelDate
            //                .add(new LabelPlaceholderBehavior())
            //                .add(new UpdateOnChangeAjaxEventBehavior())
            //                .setOutputMarkupId(true),
            new WebMarkupContainer("datePickerContainer1")
                .add(
                    datePicker1
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
                                        dateModel2.setObject(LocalDate.now());
                                        target.add(dateRange);
                                        target.add(datePicker2);
                                      }
                                    }))),
            new WebMarkupContainer("datePickerContainer2")
                .add(
                    datePicker2
                        .setRequired(true)
                        .setOutputMarkupId(true)
                        .add(new UpdateOnChangeAjaxEventBehavior())),
            new WebMarkupContainer("datePickerContainer3")
                .add(new DateTimePickerVueComponent("dateTimePicker", dateTimeModel)),
            new WebMarkupContainer("datePickerContainer4")
                .add(dateRange.add(new UpdateOnChangeAjaxEventBehavior())),
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

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(dateModel);
  }
}
