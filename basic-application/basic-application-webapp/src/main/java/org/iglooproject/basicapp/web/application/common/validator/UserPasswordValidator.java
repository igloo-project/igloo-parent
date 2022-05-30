package org.iglooproject.basicapp.web.application.common.validator;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.validation.ValidationError;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.api.util.Detachables;
import org.iglooproject.wicket.more.markup.html.form.validation.IFormModelValidator;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.UsernameRule;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class UserPasswordValidator implements IFormModelValidator {

	private static final long serialVersionUID = 5619802188558408589L;

	private static final List<String> RULES_CUSTOM_ERROR = ImmutableList.of(
		LengthRule.ERROR_CODE_MIN,
		LengthRule.ERROR_CODE_MAX
	);

	private static final String HISTORY_VIOLATION = "HISTORY_VIOLATION";

	private static final String COMMON_ERROR = "COMMON_ERROR";

	private final IModel<? extends Class<? extends User>> userClassModel;

	private IModel<? extends User> userModel;

	private final FormComponent<String> passwordFormComponent;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	@SpringBean
	private PasswordEncoder passwordEncoder;

	@SpringBean
	private IPropertyService propertyService;

	public UserPasswordValidator(IModel<? extends Class<? extends User>> userClassModel, FormComponent<String> passwordFormComponent) {
		super();
		Injector.get().inject(this);
		
		this.userClassModel = checkNotNull(userClassModel);
		this.passwordFormComponent = checkNotNull(passwordFormComponent);
	}

	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return new FormComponent<?>[] { passwordFormComponent };
	}

	@Override
	public void validate(Form<?> form) {
		String password = passwordFormComponent.getValue();
		
		if (Boolean.FALSE.equals(propertyService.get(BasicApplicationCorePropertyIds.SECURITY_PASSWORD_VALIDATOR_ENABLED)) || !StringUtils.hasText(password)) {
			return;
		}
		
		Class<? extends User> userClass = userClassModel.getObject();
		User user = userModel != null ? userModel.getObject() : null;
		String username = Bindings.user().username().getSafelyWithRoot(user);
		
		PasswordData passwordData = new PasswordData(password);
		
		List<Rule> passwordRules = Lists.newArrayList(securityManagementService.getSecurityOptions(userClass).getPasswordRules());
		
		if (StringUtils.hasText(username)) {
			passwordData.setUsername(username);
		} else {
			passwordRules.removeAll(Lists.newArrayList(Iterables.filter(passwordRules, UsernameRule.class)));
		}
		
		PasswordValidator validator = new PasswordValidator(passwordRules);
		RuleResult result = validator.validate(passwordData);
		
		boolean valid = true;
		if (!result.isValid()) {
			valid = false;
			for (RuleResultDetail detail : result.getDetails()) {
				if (RULES_CUSTOM_ERROR.contains(detail.getErrorCode())) {
					passwordFormComponent.error(
						new ValidationError().addKey(errorCodeKey(detail.getErrorCode()))
							.setVariables((Map<String, Object>) detail.getParameters())
					);
				}
			}
		}
		
		if (
				user != null
			&&	securityManagementService.getSecurityOptions(user).isPasswordHistoryEnabled()
			&&	user.getPasswordInformation().getHistory() != null
			&&	!user.getPasswordInformation().getHistory().isEmpty()
		) {
			for (String historyPasswordHash : user.getPasswordInformation().getHistory()) {
				if (passwordEncoder.matches(password, historyPasswordHash)) {
					valid = false;
					passwordFormComponent.error(new ValidationError().addKey(errorCodeKey(HISTORY_VIOLATION)));
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
		Detachables.detach(
			userClassModel,
			userModel
		);
	}

}
