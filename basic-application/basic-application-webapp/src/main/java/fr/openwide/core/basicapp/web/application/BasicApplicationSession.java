package fr.openwide.core.basicapp.web.application;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.wicket.more.AbstractCoreSession;

public class BasicApplicationSession extends AbstractCoreSession<User> {
	
	private static final long serialVersionUID = 1870827020904365541L;
	
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