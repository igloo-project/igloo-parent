package fr.openwide.core.basicapp.web.application.common.validator;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.spring.util.StringUtils;

public class EmailExistsValidator extends Behavior implements IValidator<String> {

	private static final long serialVersionUID = 1303632566302541081L;

	private static final EmailExistsValidator INSTANCE = new EmailExistsValidator();

	@SpringBean
	private IUserService userService;

	public EmailExistsValidator() {
	}

	@Override
	public void validate(IValidatable<String> validatable) {
		String email = validatable.getValue();
		if (!StringUtils.hasText(email) || userService.getByEmailCaseInsensitive(email) == null) {
			validatable.error(new ValidationError().addKey("common.validator.email.exists"));
		}
	}

	public static EmailExistsValidator get() {
		return INSTANCE;
	}
}
