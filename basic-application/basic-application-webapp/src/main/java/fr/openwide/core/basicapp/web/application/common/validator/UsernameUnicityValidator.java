package fr.openwide.core.basicapp.web.application.common.validator;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.wicket.more.util.validate.validators.AbstractUnicityValidator;

public class UsernameUnicityValidator extends AbstractUnicityValidator<User, String> {

	private static final long serialVersionUID = 7351972908406394930L;
	
	@SpringBean
	private IUserService userService;
	
	public UsernameUnicityValidator(IModel<? extends User> mainObjectModel) {
		super(mainObjectModel, "common.validator.username.unicity");
	}

	@Override
	protected User getByUniqueField(String value) {
		return userService.getByUserName(value);
	}

}
