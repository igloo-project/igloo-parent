package org.iglooproject.basicapp.web.application.security.password.template;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.common.template.ApplicationAccessTemplate;
import org.iglooproject.wicket.condition.Condition;

public abstract class SecurityPasswordTemplate extends ApplicationAccessTemplate {

	private static final long serialVersionUID = -4350860041946569108L;

	public SecurityPasswordTemplate(PageParameters parameters) {
		super(parameters);
		
		if (!keepSignedIn().applies() && BasicApplicationSession.get().isSignedIn()) {
			BasicApplicationSession.get().invalidate();
			throw new RestartResponseException(getClass(), parameters);
		}
	}

	public Condition keepSignedIn() {
		return Condition.alwaysTrue();
	}

}
