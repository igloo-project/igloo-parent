package fr.openwide.core.basicapp.web.application.common.validator;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;

public class EmailUnicityValidator extends AbstractUnicityValidator<User, String> {

	private static final long serialVersionUID = 2113279975851524261L;

	@SpringBean
	private IUserService userService;
	
	public EmailUnicityValidator(IModel<? extends User> mainObjectModel) {
		super(mainObjectModel, "common.validator.email.unicity");
	}

	@Override
	protected User getByUniqueField(String value) {
		return userService.getByEmailCaseInsensitive(value);
	}

}
