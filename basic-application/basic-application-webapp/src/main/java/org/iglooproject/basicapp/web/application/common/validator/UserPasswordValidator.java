package org.iglooproject.basicapp.web.application.common.validator;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.UsernameRule;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class UserPasswordValidator extends Behavior implements IValidator<String> {

	private static final long serialVersionUID = 5619802188558408589L;

	private static final List<String> RULES_CUSTOM_ERROR = Lists.newArrayList(
			LengthRule.ERROR_CODE_MIN,
			LengthRule.ERROR_CODE_MAX
	);

	private static final String HISTORY_VIOLATION = "HISTORY_VIOLATION";

	private static final String COMMON_ERROR = "COMMON_ERROR";

	private final IModel<? extends Class<? extends User>> userClassModel;

	private IModel<? extends User> userModel;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	@SpringBean
	private PasswordEncoder passwordEncoder;

	@SpringBean
	private IPropertyService propertyService;

	public UserPasswordValidator(IModel<? extends Class<? extends User>> userClassModel) {
		super();
		Injector.get().inject(this);
		
		this.userClassModel = checkNotNull(userClassModel);
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(
			userClassModel,
			userModel
		);
	}

	@Override
	public void validate(IValidatable<String> validatable) {
		String password = validatable.getValue();
		
		if (Boolean.FALSE.equals(propertyService.get(BasicApplicationCorePropertyIds.SECURITY_PASSWORD_VALIDATOR_ENABLED)) || !StringUtils.hasText(password)) {
			return;
		}
		
		Class<? extends User> userClass = userClassModel.getObject();
		User user = userModel != null ? userModel.getObject() : null;
		
		PasswordData passwordData = new PasswordData(password);
		
		List<Rule> passwordRules = Lists.newArrayList(securityManagementService.getOptions(userClass).getPasswordRules());
		
		if (user != null && StringUtils.hasText(user.getUsername())) {
			passwordData.setUsername(user.getUsername());
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
					validatable.error(
						new ValidationError(this, detail.getErrorCode())
							.setVariables((Map<String, Object>) detail.getParameters())
					);
				}
			}
		}
		
		if (
				user != null
			&&	securityManagementService.getOptions(user).isPasswordHistoryEnabled()
			&&	user.getPasswordInformation().getHistory() != null
			&&	!user.getPasswordInformation().getHistory().isEmpty()
		) {
			for (String historyPasswordHash : user.getPasswordInformation().getHistory()) {
				if (passwordEncoder.matches(password, historyPasswordHash)) {
					valid = false;
					validatable.error(new ValidationError(this, HISTORY_VIOLATION));
					break;
				}
			}
		}
		
		if (!valid) {
			validatable.error(new ValidationError(this, COMMON_ERROR));
		}
	}

	public UserPasswordValidator userModel(IModel<? extends User> userModel) {
		this.userModel = checkNotNull(userModel);
		return this;
	}

}
