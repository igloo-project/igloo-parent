package basicapp.front.administration.form;

import static basicapp.back.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_LENGTH_MIN;

import basicapp.back.business.user.model.TechnicalUser;
import basicapp.back.business.user.typedescriptor.UserTypeDescriptor;
import basicapp.back.util.binding.Bindings;
import basicapp.front.administration.page.AdministrationTechnicalUserDetailPage;
import basicapp.front.common.model.UserTypeDescriptorModel;
import basicapp.front.common.validator.EmailUnicityValidator;
import basicapp.front.common.validator.UserPasswordValidator;
import basicapp.front.common.validator.UsernameUnicityValidator;
import igloo.igloojs.showpassword.ShowPasswordBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.iglooproject.wicket.more.markup.html.form.LocaleDropDownChoice;
import org.iglooproject.wicket.more.markup.html.form.ModelValidatingForm;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;

public class TechnicalUserPopup extends AbstractUserPopup<TechnicalUser> {

  private static final long serialVersionUID = -1282527654253780436L;

  private final IModel<? extends UserTypeDescriptor<TechnicalUser>> userTypeDescriptorModel;

  public TechnicalUserPopup(String id) {
    super(id);

    this.userTypeDescriptorModel = UserTypeDescriptorModel.fromUser(getModel());
  }

  @Override
  protected Component createBody(String wicketId) {
    DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());

    form = new ModelValidatingForm<>("form", getModel());
    body.add(form);

    boolean passwordRequired =
        securityManagementService
                .getSecurityOptions(TechnicalUser.class)
                .isPasswordAdminUpdateEnabled()
            && !securityManagementService
                .getSecurityOptions(TechnicalUser.class)
                .isPasswordUserRecoveryEnabled();

    PasswordTextField password = new PasswordTextField("password", passwordModel);

    form.add(
        new TextField<String>("firstName", BindingModel.of(getModel(), Bindings.user().firstName()))
            .setLabel(new ResourceModel("business.user.firstName"))
            .setRequired(true),
        new TextField<String>("lastName", BindingModel.of(getModel(), Bindings.user().lastName()))
            .setLabel(new ResourceModel("business.user.lastName"))
            .setRequired(true),
        new TextField<String>("username", BindingModel.of(getModel(), Bindings.user().username()))
            .setLabel(new ResourceModel("business.user.username"))
            .setRequired(true)
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
                                securityManagementService
                                    .getSecurityOptions(TechnicalUser.class)
                                    .isPasswordAdminUpdateEnabled()))
                    .add(
                        password
                            .setLabel(new ResourceModel("business.user.password"))
                            .setRequired(passwordRequired),
                        new BlankLink("showPassword").add(new ShowPasswordBehavior(password)),
                        new CoreLabel(
                            "passwordHelp",
                            new StringResourceModel(
                                    "security.${resourceKeyBase}.password.help",
                                    userTypeDescriptorModel)
                                .setParameters(
                                    ApplicationPropertyModel.of(SECURITY_PASSWORD_LENGTH_MIN))
                                .setDefaultValue(
                                    new StringResourceModel("security.user.password.help")
                                        .setParameters(
                                            ApplicationPropertyModel.of(
                                                SECURITY_PASSWORD_LENGTH_MIN))))),
                new CheckBox("enabled", BindingModel.of(getModel(), Bindings.user().enabled()))
                    .setLabel(new ResourceModel("business.user.enabled"))
                    .setOutputMarkupId(true)));

    form.add(
        new UserPasswordValidator(
                userTypeDescriptorModel.map(UserTypeDescriptor::getClazz), password)
            .userModel(getModel()));

    return body;
  }

  @Override
  protected void doOnSuccess(IModel<TechnicalUser> userModel) {
    throw AdministrationTechnicalUserDetailPage.MAPPER
        .ignoreParameter2()
        .map(userModel)
        .newRestartResponseException();
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(userTypeDescriptorModel);
  }
}
