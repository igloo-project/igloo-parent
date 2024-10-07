package basicapp.front.security.password.component;

import static basicapp.back.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_LENGTH_MIN;

import basicapp.back.business.user.model.User;
import basicapp.back.security.service.controller.ISecurityManagementControllerService;
import basicapp.back.util.binding.Bindings;
import basicapp.front.common.validator.UserPasswordValidator;
import igloo.igloojs.showpassword.ShowPasswordBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.form.ModelValidatingForm;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.iglooproject.wicket.more.util.validate.validators.PredicateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityPasswordResetContentPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(SecurityPasswordResetContentPanel.class);

  @SpringBean private ISecurityManagementControllerService securityManagementControllerService;

  private final IModel<String> emailModel = Model.of();

  private final IModel<String> passwordModel = Model.of();

  public SecurityPasswordResetContentPanel(String wicketId, IModel<User> userModel) {
    super(wicketId, userModel);

    ModelValidatingForm<?> form = new ModelValidatingForm<>("form");
    add(form);

    TextField<String> password = new PasswordTextField("password", passwordModel);

    form.add(
        new TextField<>("email", emailModel)
            .setLabel(new ResourceModel("business.user.email"))
            .setRequired(true)
            .add(new LabelPlaceholderBehavior())
            .add(
                PredicateValidator.of(
                        email -> email != null && email.equals(getModelObject().getEmail()))
                    .errorKey("common.validator.email.match.user"))
            .setOutputMarkupId(true),
        password
            .setLabel(new ResourceModel("business.user.newPassword"))
            .setRequired(true)
            .add(new LabelPlaceholderBehavior())
            .setOutputMarkupId(true),
        new BlankLink("showPassword").add(new ShowPasswordBehavior(password)),
        new CoreLabel(
            "passwordHelp",
            new StringResourceModel("user.common.form.password.help")
                .setParameters(ApplicationPropertyModel.of(SECURITY_PASSWORD_LENGTH_MIN))));

    form.add(
        new UserPasswordValidator(BindingModel.of(getModel(), Bindings.user().type()), password)
            .userModel(getModel()));

    form.add(
        new AjaxButton("validate", form) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            try {
              User user = SecurityPasswordResetContentPanel.this.getModelObject();
              securityManagementControllerService.updatePassword(user, passwordModel.getObject());

              Session.get().success(getString("security.password.reset.validate.success"));

              throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
            } catch (RestartResponseException e) {
              throw e;
            } catch (Exception e) {
              LOGGER.error("Error occurred while resetting password", e);
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
    Detachables.detach(emailModel, passwordModel);
  }
}
