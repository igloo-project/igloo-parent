package basicapp.front.security.password.component;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.user.service.controller.IUserControllerService;
import basicapp.front.common.form.EmailAddressTextField;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.model.Detachables;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.application.CoreWicketAuthenticatedApplication;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityPasswordRecoveryRequestResetContentPanel extends Panel {

  private static final long serialVersionUID = 547223775134254240L;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(SecurityPasswordRecoveryRequestResetContentPanel.class);

  @SpringBean private IUserControllerService userControllerService;

  private final IModel<EmailAddress> emailAddressModel = Model.of();

  public SecurityPasswordRecoveryRequestResetContentPanel(String wicketId) {
    super(wicketId);

    Form<?> form = new Form<>("form");
    add(form);

    form.add(
        new EmailAddressTextField("emailAddress", emailAddressModel)
            .setLabel(new ResourceModel("business.user.emailAddress"))
            .setRequired(true)
            .add(new LabelPlaceholderBehavior())
            .setOutputMarkupId(true));

    form.add(
        new AjaxButton("validate", form) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            try {
              userControllerService.initPasswordRecoveryRequest(emailAddressModel.getObject());

              Session.get()
                  .success(getString("security.password.recovery.request.reset.validate.success"));

              throw CoreWicketAuthenticatedApplication.get()
                  .getSignInPageLinkDescriptor()
                  .newRestartResponseException();
            } catch (RestartResponseException e) {
              throw e;
            } catch (Exception e) {
              LOGGER.error("Error occurred while requesting password reset.", e);
              Session.get().error(getString("common.error.unexpected"));
            }

            FeedbackUtils.refreshFeedback(target, getPage());
          }

          @Override
          protected void onError(AjaxRequestTarget target) {
            FeedbackUtils.refreshFeedback(target, getPage());
          }
        });
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(emailAddressModel);
  }
}
