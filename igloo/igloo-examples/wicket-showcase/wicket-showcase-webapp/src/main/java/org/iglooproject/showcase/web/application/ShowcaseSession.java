package org.iglooproject.showcase.web.application;

import org.apache.wicket.Session;
import org.apache.wicket.request.Request;

import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.wicket.more.AbstractCoreSession;

public class ShowcaseSession extends AbstractCoreSession<User> {
	private static final long serialVersionUID = 3232447384256486862L;
	
	public ShowcaseSession(Request request) {
		super(request);
	}
	
	public static ShowcaseSession get() {
		return (ShowcaseSession) Session.get();
	}

}
