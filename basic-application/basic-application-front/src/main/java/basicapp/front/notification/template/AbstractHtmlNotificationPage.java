package basicapp.front.notification.template;

import basicapp.back.config.util.Environment;
import basicapp.back.property.BasicApplicationBackPropertyIds;
import basicapp.front.BasicApplicationApplication;
import basicapp.front.common.template.resources.styles.notification.email.NotificationEmailScssResourceReference;
import basicapp.front.common.template.resources.styles.notification.head.NotificationHeadScssResourceReference;
import basicapp.front.notification.component.EnvironmentContainerPanel;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.Detachables;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.markup.html.CoreWebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHtmlNotificationPage<T> extends CoreWebPage {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHtmlNotificationPage.class);

  @SpringBean private IPropertyService propertyService;

  private final IModel<Locale> localeModel;

  protected AbstractHtmlNotificationPage(IModel<Locale> localeModel) {
    this(null, localeModel);
  }

  protected AbstractHtmlNotificationPage(IModel<T> model, IModel<Locale> localeModel) {
    super(model);

    this.localeModel = Objects.requireNonNull(localeModel);
    IModel<Environment> environmentModel =
        Model.of(propertyService.get(BasicApplicationBackPropertyIds.ENVIRONMENT));

    add(
        new TransparentWebMarkupContainer("htmlElement")
            .add(AttributeAppender.append("lang", getLocale())));

    add(
        new TransparentWebMarkupContainer("bodyElement")
            .add(new ClassAttributeAppender(environmentModel))
            .add(AttributeAppender.append("lang", getLocale())));

    add(new EnvironmentContainerPanel("environment", environmentModel));

    add(
        new ExternalLink(
            "homePageLink",
            LoadableDetachableModel.of(
                () ->
                    BasicApplicationApplication.get()
                        .getHomePageLinkDescriptor()
                        .bypassPermissions()
                        .fullUrl())));
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();

    add(new CoreLabel("preview", getPreviewModel()).hideIfEmpty());
  }

  protected IModel<?> getPreviewModel() {
    return Model.of();
  }

  @Override
  public void renderHead(IHeaderResponse response) {
    try (InputStream isEmail =
            ((PackageResource) NotificationEmailScssResourceReference.get().getResource())
                .getResourceStream()
                .getInputStream();
        InputStream isHead =
            ((PackageResource) NotificationHeadScssResourceReference.get().getResource())
                .getResourceStream()
                .getInputStream()) {
      response.render(
          CssContentHeaderItem.forCSS(
              IOUtils.toString(isEmail, StandardCharsets.UTF_8), "notification-email"));
      response.render(
          CssContentHeaderItem.forCSS(
              IOUtils.toString(isHead, StandardCharsets.UTF_8), "notification-head"));
    } catch (IOException | ResourceStreamNotFoundException e) {
      LOGGER.error("Error on load notification css.", e);
    }
  }

  @Override
  public Locale getLocale() {
    return localeModel.getObject();
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(localeModel);
  }
}
