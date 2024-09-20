package basicapp.front.common.validator;

import static com.google.common.base.Preconditions.checkNotNull;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.property.BasicApplicationCorePropertyIds;
import basicapp.back.security.service.controller.ISecurityManagementControllerService;
import basicapp.back.util.binding.Bindings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import igloo.wicket.model.Detachables;
import java.util.List;
import java.util.Map;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.validation.ValidationError;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.markup.html.form.validation.IFormModelValidator;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.UsernameRule;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserPasswordValidator implements IFormModelValidator {

  private static final long serialVersionUID = 5619802188558408589L;

  private static final List<String> RULES_CUSTOM_ERROR =
      ImmutableList.of(LengthRule.ERROR_CODE_MIN, LengthRule.ERROR_CODE_MAX);

  private static final String HISTORY_VIOLATION = "HISTORY_VIOLATION";

  private static final String COMMON_ERROR = "COMMON_ERROR";

  private final IModel<UserType> userTypeModel;

  private IModel<? extends User> userModel;

  private final FormComponent<String> passwordFormComponent;

  @SpringBean private ISecurityManagementControllerService securityManagementController;

  @SpringBean private PasswordEncoder passwordEncoder;

  @SpringBean private IPropertyService propertyService;

  public UserPasswordValidator(
      IModel<UserType> userTypeModel, FormComponent<String> passwordFormComponent) {
    super();
    Injector.get().inject(this);
    this.userTypeModel = checkNotNull(userTypeModel);
    this.passwordFormComponent = checkNotNull(passwordFormComponent);
  }

  @Override
  public FormComponent<?>[] getDependentFormComponents() {
    return new FormComponent<?>[] {passwordFormComponent};
  }

  @Override
  public void validate(Form<?> form) {
    String password = passwordFormComponent.getValue();

    if (Boolean.FALSE.equals(
            propertyService.get(
                BasicApplicationCorePropertyIds.SECURITY_PASSWORD_VALIDATOR_ENABLED))
        || !StringUtils.hasText(password)) {
      return;
    }

    User user = userModel != null ? userModel.getObject() : null;
    String username = Bindings.user().username().getSafelyWithRoot(user);

    PasswordData passwordData = new PasswordData(password);

    List<Rule> passwordRules =
        Lists.newArrayList(
            securityManagementController
                .getSecurityOptions(userTypeModel.getObject())
                .getPasswordRules());

    if (StringUtils.hasText(username)) {
      passwordData.setUsername(username);
    } else {
      passwordRules.removeAll(
          Lists.newArrayList(Iterables.filter(passwordRules, UsernameRule.class)));
    }

    PasswordValidator validator = new PasswordValidator(passwordRules);
    RuleResult result = validator.validate(passwordData);

    boolean valid = true;
    if (!result.isValid()) {
      valid = false;
      for (RuleResultDetail detail : result.getDetails()) {
        if (RULES_CUSTOM_ERROR.contains(detail.getErrorCode())) {
          passwordFormComponent.error(
              new ValidationError()
                  .addKey(errorCodeKey(detail.getErrorCode()))
                  .setVariables((Map<String, Object>) detail.getParameters()));
        }
      }
    }

    if (user != null
        && securityManagementController.getSecurityOptions(user).isPasswordHistoryEnabled()
        && user.getPasswordInformation().getHistory() != null
        && !user.getPasswordInformation().getHistory().isEmpty()) {
      for (String historyPasswordHash : user.getPasswordInformation().getHistory()) {
        if (passwordEncoder.matches(password, historyPasswordHash)) {
          valid = false;
          passwordFormComponent.error(
              new ValidationError().addKey(errorCodeKey(HISTORY_VIOLATION)));
          break;
        }
      }
    }

    if (!valid) {
      passwordFormComponent.error(new ValidationError().addKey(errorCodeKey(COMMON_ERROR)));
    }
  }

  public UserPasswordValidator userModel(IModel<? extends User> userModel) {
    this.userModel = checkNotNull(userModel);
    return this;
  }

  protected String errorCodeKey(String errorCode) {
    if (!StringUtils.hasText(errorCode)) {
      throw new IllegalStateException();
    }
    return errorCodeKeyPrefix() + "." + errorCode;
  }

  protected String errorCodeKeyPrefix() {
    return Classes.simpleName(UserPasswordValidator.class);
  }

  @Override
  public void detach() {
    Detachables.detach(userModel);
  }
}
