package fr.openwide.core.basicapp.web.application.common.validator;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.Lists;

import edu.vt.middleware.password.LengthRule;
import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.RuleResult;
import edu.vt.middleware.password.RuleResultDetail;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.spring.util.StringUtils;

public class UserPasswordValidator extends Behavior implements IValidator<String> {

	private static final long serialVersionUID = 5619802188558408589L;

	private static final List<String> RULES_CUSTOM_ERROR = Lists.newArrayList(
			LengthRule.ERROR_CODE_MIN,
			LengthRule.ERROR_CODE_MAX
	);

	private static final String HISTORY_VIOLATION = "HISTORY_VIOLATION";

	private static final String COMMON_ERROR = "COMMON_ERROR";

	private final UserTypeDescriptor<?> typeDescriptor;

	private IModel<? extends User> userModel;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	@SpringBean
	private PasswordEncoder passwordEncoder;

	@SpringBean
	private BasicApplicationConfigurer configurer;

	public UserPasswordValidator(UserTypeDescriptor<?> typeDescriptor) {
		super();
		Injector.get().inject(this);
		
		this.typeDescriptor = checkNotNull(typeDescriptor);
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		if (userModel != null) {
			userModel.detach();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validate(IValidatable<String> validatable) {
		String password = validatable.getValue();
		
		if (!configurer.isSecurityPasswordValidatorEnabled() || !StringUtils.hasText(password)) {
			return;
		}
		
		PasswordValidator validator = new PasswordValidator(Lists.newArrayList(securityManagementService.getOptions(typeDescriptor.getEntityClass()).getPasswordRules().getRules()));
		PasswordData passwordData = new PasswordData(new Password(password));
		
		if (userModel != null && userModel.getObject() != null && StringUtils.hasText(userModel.getObject().getUserName())) {
			passwordData.setUsername(userModel.getObject().getUserName());
		}
		
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
		
		if (userModel != null && userModel.getObject() != null
				&& userModel.getObject().getPasswordInformation().getHistory() != null
				&& userModel.getObject().getPasswordInformation().getHistory().isEmpty()) {
			String passwordHash = passwordEncoder.encode(password);
			if (userModel.getObject().getPasswordInformation().getHistory().contains(passwordHash)) {
				valid = false;
				validatable.error(
						new ValidationError(this, HISTORY_VIOLATION)
				);
			}
		}
		
		if (!valid) {
			validatable.error(
					new ValidationError(this, COMMON_ERROR)
			);
		}
	}

	public UserPasswordValidator userModel(IModel<? extends User> userModel) {
		this.userModel = checkNotNull(userModel);
		return this;
	}
}
