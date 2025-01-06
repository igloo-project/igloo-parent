package basicapp.front.user.popup;

import static basicapp.back.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_LENGTH_MIN;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.service.controller.IUserControllerService;
import basicapp.back.security.service.controller.ISecurityManagementControllerService;
import basicapp.back.util.binding.Bindings;
import basicapp.front.BasicApplicationSession;
import basicapp.front.common.validator.EmailUnicityValidator;
import basicapp.front.common.validator.UserPasswordValidator;
import basicapp.front.common.validator.UsernamePatternValidator;
import basicapp.front.common.validator.UsernameUnicityValidator;
import basicapp.front.user.page.BasicUserDetailPage;
import igloo.bootstrap.modal.AbstractAjaxModalPopupPanel;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import java.util.Collections;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.markup.html.form.FormMode;
import org.iglooproject.wicket.more.markup.html.form.LocaleDropDownChoice;
import org.iglooproject.wicket.more.markup.html.form.ModelValidatingForm;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicUserSavePopup extends AbstractAjaxModalPopupPanel<User> {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(BasicUserSavePopup.class);

  private static final UsernamePatternValidator USERNAME_PATTERN_VALIDATOR =
      new UsernamePatternValidator() {
        private static final long serialVersionUID = 1L;

        @Override
        protected IValidationError decorate(
            IValidationError error, IValidatable<String> validatable) {
          ((ValidationError) error)
              .setKeys(Collections.singletonList("common.validator.username.pattern"));
          return error;
        }
      };

  @SpringBean private IUserControllerService userControllerService;

  @SpringBean private ISecurityManagementControllerService securityManagementControllerService;

  @SpringBean private IPropertyService propertyService;

  private final IModel<FormMode> formModeModel = Model.of();

  private final IModel<String> passwordModel = Model.of();

  private ModelValidatingForm<User> form;

  public BasicUserSavePopup(String id) {
    super(id, new GenericEntityModel<>());
  }

  @Override
  protected Component createHeader(String wicketId) {
    return new CoreLabel(
        wicketId,
        addModeCondition()
            .then(new ResourceModel("user.add.title"))
            .otherwise(new StringResourceModel("user.edit.title", getModel())));
  }

  @Override
  protected Component createBody(String wicketId) {
    DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());

    form = new ModelValidatingForm<>("form", getModel());
    body.add(form);

    boolean passwordRequired =
        securityManagementControllerService
                .getSecurityOptions(UserType.BASIC)
                .isPasswordAdminUpdateEnabled()
            && !securityManagementControllerService
                .getSecurityOptions(UserType.BASIC)
                .isPasswordUserRecoveryEnabled();

    PasswordTextField password = new PasswordTextField("password", passwordModel);

    form.add(
        new RequiredTextField<>(
                "firstName", BindingModel.of(getModel(), Bindings.user().firstName()))
            .setLabel(new ResourceModel("business.user.firstName")),
        new RequiredTextField<>("lastName", BindingModel.of(getModel(), Bindings.user().lastName()))
            .setLabel(new ResourceModel("business.user.lastName")),
        new RequiredTextField<>("username", BindingModel.of(getModel(), Bindings.user().username()))
            .setLabel(new ResourceModel("business.user.username"))
            .add(USERNAME_PATTERN_VALIDATOR)
            .add(new UsernameUnicityValidator(getModel())),
        new EmailTextField("email", BindingModel.of(getModel(), Bindings.user().email()))
            .setLabel(new ResourceModel("business.user.email"))
            .add(EmailAddressValidator.getInstance())
            .add(new EmailUnicityValidator(getModel())),
        new LocaleDropDownChoice("locale", BindingModel.of(getModel(), Bindings.user().locale()))
            .setLabel(new ResourceModel("business.user.locale"))
            .setRequired(true),
        new EnclosureContainer("addContainer")
            .condition(addModeCondition())
            .add(
                new EnclosureContainer("passwordContainer")
                    .condition(
                        Condition.isTrue(
                            () ->
                                securityManagementControllerService
                                    .getSecurityOptions(UserType.BASIC)
                                    .isPasswordAdminUpdateEnabled()))
                    .add(
                        password
                            .setLabel(new ResourceModel("business.user.password"))
                            .setRequired(passwordRequired),
                        new CoreLabel(
                            "passwordHelp",
                            new StringResourceModel("user.common.form.password.help")
                                .setParameters(
                                    ApplicationPropertyModel.of(SECURITY_PASSWORD_LENGTH_MIN)))),
                new CheckBox("enabled", BindingModel.of(getModel(), Bindings.user().enabled()))
                    .setLabel(new ResourceModel("business.user.enabled"))
                    .setOutputMarkupId(true)));

    form.add(new UserPasswordValidator(Model.of(UserType.BASIC), password).userModel(getModel()));

    return body;
  }

  @Override
  protected Component createFooter(String wicketId) {
    DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, BasicUserSavePopup.class);

    footer.add(
        new AjaxButton("save", form) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            try {
              IModel<User> userModel = BasicUserSavePopup.this.getModel();
              User user = userModel.getObject();
              User authenticatedUser = BasicApplicationSession.get().getUser();
              String password = passwordModel.getObject();

              userControllerService.saveBasicUser(user, password);

              Session.get().success(getString("common.success"));

              if (addModeCondition().applies() && !StringUtils.hasText(password)) {
                Session.get().success(getString("user.add.success.notification"));
                throw BasicUserDetailPage.MAPPER
                    .ignoreParameter2()
                    .map(userModel)
                    .newRestartResponseException();
              } else {
                if (authenticatedUser != null
                    && authenticatedUser.equals(user)
                    && user.getLocale() != null) {
                  BasicApplicationSession.get().setLocale(user.getLocale());
                }
                closePopup(target);
                target.add(getPage());
              }
            } catch (RestartResponseException e) { // NOSONAR
              throw e;
            } catch (Exception e) {
              if (addModeCondition().applies()) {
                LOGGER.error("Error occured while creating user", e);
              } else {
                LOGGER.error("Error occured while updating user", e);
              }
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

  public void setUpAdd(User user) {
    formModeModel.setObject(FormMode.ADD);
    if (user.getLocale() == null) {
      user.setLocale(propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
    }
    getModel().setObject(user);
  }

  public void setUpEdit(User user) {
    formModeModel.setObject(FormMode.EDIT);
    if (user.getLocale() == null) {
      user.setLocale(propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
    }
    getModel().setObject(user);
  }

  private Condition addModeCondition() {
    return FormMode.ADD.condition(formModeModel);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(formModeModel, passwordModel);
  }
}
