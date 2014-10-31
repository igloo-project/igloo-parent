package fr.openwide.core.basicapp.web.application.security.login.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.security.login.template.SignInTemplate;
import fr.openwide.core.basicapp.web.application.security.login.util.SignInUserTypeDescriptor;

public class SignInPage extends SignInTemplate<User> {

	private static final long serialVersionUID = 7361718534092594202L;

	public SignInPage(PageParameters parameters) {
		super(parameters, SignInUserTypeDescriptor.DEFAULT);
	}

}
