package fr.openwide.core.basicapp.web.application.security.login.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.security.login.template.SignInTemplate;

public class SignInPage extends SignInTemplate<User> {

	private static final long serialVersionUID = 7361718534092594202L;

	public SignInPage(PageParameters parameters) {
		super(parameters, UserTypeDescriptor.USER);
	}

}
