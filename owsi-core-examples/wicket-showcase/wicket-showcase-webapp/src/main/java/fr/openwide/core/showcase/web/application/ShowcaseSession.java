package fr.openwide.core.showcase.web.application;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;

import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class ShowcaseSession extends AbstractCoreSession<User> {
	private static final long serialVersionUID = 3232447384256486862L;
	
	public ShowcaseSession(Request request) {
		super(request);
	}
	
	public static ShowcaseSession get() {
		return (ShowcaseSession) Session.get();
	}
	
	public User getUser() {
		return super.getPerson();
	}
	
	public IModel<User> getUserModel() {
		return new GenericEntityModel<Long, User>(getUser());
	}
	
	public boolean hasRoleAdmin() {
		return hasRole(CoreAuthorityConstants.ROLE_ADMIN);
	}
}
