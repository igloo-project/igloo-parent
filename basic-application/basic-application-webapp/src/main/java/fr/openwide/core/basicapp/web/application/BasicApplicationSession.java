package fr.openwide.core.basicapp.web.application;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
	
	@Override
	public boolean authenticate(String username, String password) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return super.authenticate(username, password);
	}
	
	public User getUser() {
		return super.getPerson();
	}
	
	public IModel<User> getUserModel() {
		return super.getPersonModel();
	}
}