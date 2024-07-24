package basicapp.front.console.notification.demo.page;

import basicapp.back.business.common.util.BasicApplicationLocale;
import basicapp.front.console.notification.demo.template.ConsoleNotificationDemoTemplate;
import com.google.common.base.Throwables;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.Detachables;
import java.util.Locale;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.mail.api.SimpleRecipient;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.wicket.more.ajax.AjaxListeners;
import org.iglooproject.wicket.more.behavior.IFrameContentBehavior;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.markup.html.form.LocaleDropDownChoice;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleNotificationDemoDetailPage extends ConsoleNotificationDemoTemplate {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ConsoleNotificationDemoDetailPage.class);

  private final IModel<INotificationContentDescriptor> descriptorModel;

  private final IModel<Locale> localeModel = Model.of(BasicApplicationLocale.DEFAULT);
  private final IModel<INotificationContentDescriptor> descriptorWithContextModel;
  private final IModel<String> subjectModel;
  private final IModel<String> bodyModel;

  public ConsoleNotificationDemoDetailPage(
      PageParameters parameters, IModel<INotificationContentDescriptor> descriptorModel) {
    super(parameters);

    addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("console.notifications")));

    this.descriptorModel = descriptorModel;

    descriptorWithContextModel =
        LoadableDetachableModel.of(
            () ->
                descriptorModel
                    .getObject()
                    .withContext(new SimpleRecipient(localeModel.getObject(), null, null)));

    subjectModel =
        LoadableDetachableModel.of(
            () -> {
              try {
                return descriptorWithContextModel.getObject().renderSubject();
              } catch (NotificationContentRenderingException | RuntimeException e) {
                LOGGER.error("Error on render subject.", e);
                return Throwables.getStackTraceAsString(e);
              }
            });

    bodyModel =
        LoadableDetachableModel.of(
            () -> {
              try {
                return descriptorWithContextModel.getObject().renderBody().getHtmlText();
              } catch (NotificationContentRenderingException | RuntimeException e) {
                LOGGER.error("Error on render body.", e);
                return "<pre>" + Throwables.getStackTraceAsString(e) + "</pre>";
              }
            });

    add(
        new Form<>("form")
            .add(
                new LocaleDropDownChoice("locale", localeModel)
                    .setLabel(new ResourceModel("console.notification.demo.detail.locale"))
                    .setRequired(true)
                    .add(
                        new UpdateOnChangeAjaxEventBehavior()
                            .onChange(AjaxListeners.refreshPage()))));

    IFrameContentBehavior iFrameContentBehavior = new IFrameContentBehavior(bodyModel);
    add(iFrameContentBehavior);

    add(
        new CoreLabel("subject", subjectModel).hideIfEmpty(),
        new WebMarkupContainer("body")
            .add(
                new AttributeModifier(
                    "src",
                    () -> urlForListener(iFrameContentBehavior, new PageParameters()).toString())));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(
        descriptorModel, localeModel, descriptorWithContextModel, subjectModel, bodyModel);
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return ConsoleNotificationDemoDetailPage.class;
  }
}
