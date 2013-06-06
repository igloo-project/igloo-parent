package fr.openwide.core.basicapp.web.application;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.authentication.AuthenticationManager;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.wicket.more.AbstractCoreSession;

public class BasicApplicationSession extends AbstractCoreSession<User> {
	
	private static final long serialVersionUID = 1870827020904365541L;
	
	@SpringBean(name = "authenticationManager")
	private AuthenticationManager authenticationManager;
	
	public BasicApplicationSession(Request request) {
		super(request);
	}
	
	public static BasicApplicationSession get() {
		return (BasicApplicationSession) Session.get();
	}
	
	public User getUser() {
		return super.getPerson();
	}
	
	public IModel<User> getUserModel() {
		return super.getPersonModel();
	}
}