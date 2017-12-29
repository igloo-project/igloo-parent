package org.iglooproject.basicapp.web.application;

import org.apache.wicket.Session;
import org.apache.wicket.request.Request;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.wicket.more.AbstractCoreSession;

public class BasicApplicationSession extends AbstractCoreSession<User> {
	
	private static final long serialVersionUID = 1870827020904365541L;
	
	public BasicApplicationSession(Request request) {
		super(request);
	}
	
	public static BasicApplicationSession get() {
		return (BasicApplicationSession) Session.get();
	}
}