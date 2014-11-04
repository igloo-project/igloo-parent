package fr.openwide.core.basicapp.web.application.common.validator;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import com.google.common.collect.Lists;

import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.RuleResult;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.security.model.SecurityOptions;

public class UserPasswordValidator<U extends User> implements IValidator<String> {

	private static final long serialVersionUID = 5619802188558408589L;

	private final IModel<String> usernameModel;

	private final SecurityOptions securityOptions;

	public UserPasswordValidator(SecurityOptions securityOptions, IModel<String> usernameModel) {
		this.securityOptions = securityOptions;
		this.usernameModel = usernameModel;
	}

	@Override
	public void validate(IValidatable<String> validatable) {
		PasswordValidator validator = new PasswordValidator(Lists.newArrayList(securityOptions.getPasswordRules().getRules()));
		PasswordData passwordData = new PasswordData(new Password(validatable.getValue()));
		passwordData.setUsername(usernameModel.getObject());
		RuleResult result = validator.validate(passwordData);
		
		if (!result.isValid()) {
//			for (RuleResultDetail detail : result.getDetails()) {
//				// TODO FLA
//			}
		}
		
		// TODO FLA
	}

}
