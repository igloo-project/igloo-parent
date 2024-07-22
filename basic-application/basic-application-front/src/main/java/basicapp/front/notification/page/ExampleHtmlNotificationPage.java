package basicapp.front.notification.page;

import basicapp.back.business.user.model.User;
import basicapp.back.util.binding.Bindings;
import basicapp.back.util.time.DateTimePattern;
import basicapp.front.BasicApplicationApplication;
import basicapp.front.administration.template.AdministrationUserDetailTemplate;
import basicapp.front.common.renderer.UserEnabledRenderer;
import basicapp.front.notification.model.InstantToDateModel;
import basicapp.front.notification.template.AbstractHtmlNotificationPage;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.renderer.Renderer;
import java.time.Instant;
import java.util.Locale;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.behavior.BootstrapColorBehavior;

public class ExampleHtmlNotificationPage extends AbstractHtmlNotificationPage<User> {

  private static final long serialVersionUID = 1L;

  public ExampleHtmlNotificationPage(
      IModel<User> userModel, IModel<Instant> instantModel, IModel<Locale> localeModel) {
    super(userModel, localeModel);

    add(
        new WebMarkupContainer("intro")
            .add(
                new CoreLabel("user", userModel).showPlaceholder(),
                new CoreLabel(
                        "date",
                        Renderer.fromDateTimePattern(DateTimePattern.SHORT_DATE)
                            .asModel(instantModel))
                    .showPlaceholder(),
                new CoreLabel(
                        "time",
                        Renderer.fromDateTimePattern(DateTimePattern.TIME).asModel(instantModel))
                    .showPlaceholder()));

    IModel<String> urlModel =
        LoadableDetachableModel.of(
            () ->
                BasicApplicationApplication.get()
                    .getHomePageLinkDescriptor()
                    .bypassPermissions()
                    .fullUrl());

    add(new ExternalLink("link", urlModel), new ExternalLink("url", urlModel, urlModel));

    add(
        new ExternalLink(
            "userLink",
            LoadableDetachableModel.of(
                () ->
                    AdministrationUserDetailTemplate.mapper()
                        .ignoreParameter2()
                        .map(userModel)
                        .bypassPermissions()
                        .fullUrl()),
            BindingModel.of(userModel, Bindings.user().username())),
        new CoreLabel("firstname", BindingModel.of(userModel, Bindings.user().firstName())),
        new CoreLabel("lastname", BindingModel.of(userModel, Bindings.user().lastName())),
        new CoreLabel("enabled", UserEnabledRenderer.get().asModel(userModel))
            .showPlaceholder()
            .add(
                BootstrapColorBehavior.text(
                    UserEnabledRenderer.get().asModel(userModel).getColorModel())),
        new CoreLabel(
            "lastLoginDate",
            new InstantToDateModel(BindingModel.of(userModel, Bindings.user().lastLoginDate()))));
  }

  @Override
  protected IModel<?> getPreviewModel() {
    return new StringResourceModel("notification.panel.example.preview");
  }
}
