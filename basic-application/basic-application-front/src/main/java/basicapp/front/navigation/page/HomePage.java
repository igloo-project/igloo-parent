package basicapp.front.navigation.page;

import basicapp.front.common.template.MainTemplate;
import basicapp.front.profile.page.ProfilePage;
import basicapp.front.referencedata.page.ReferenceDataPage;
import basicapp.front.user.page.BasicUserListPage;
import igloo.bootstrap.jsmodel.JsHelpers;
import igloo.vuedatepicker.JsDatePicker;
import igloo.vuedatepicker.VueBehavior;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.Detachables;
import java.util.Date;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class HomePage extends MainTemplate {

  private static final long serialVersionUID = -6767518941118385548L;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(HomePage.class);
  }

  private final IModel<Date> dateModel = Model.of(new Date());

  public HomePage(PageParameters parameters) {
    super(parameters);

    Form form = new Form<>("form");
    JsDatePicker jsDatePicker =
        JsDatePicker.builder()
            .autoApply(JsHelpers.of(true))
            .multiCalendars(JsHelpers.of(true))
            .dateModel(JsHelpers.of(dateModel.getObject().getTime()))
            .required(JsHelpers.of(true))
            //            .onUpdateModel()
            .build();

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("navigation.home"), HomePage.linkDescriptor()));
    TextField<Date> labelDate = new TextField<>("labelDate", dateModel);
    add(
        form.add(
            labelDate
                .add(new LabelPlaceholderBehavior())
                .add(new UpdateOnChangeAjaxEventBehavior())
                .setOutputMarkupId(true),
            new WebMarkupContainer("vue-datepicker-container")
                .add(
                    new VueBehavior(jsDatePicker, dateModel) {
                      private static final long serialVersionUID = 1L;

                      @Override
                      protected void respond(AjaxRequestTarget target) {
                        super.respond(target);
                        labelDate.clearInput();
                        target.add(labelDate);
                      }
                    })));
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
