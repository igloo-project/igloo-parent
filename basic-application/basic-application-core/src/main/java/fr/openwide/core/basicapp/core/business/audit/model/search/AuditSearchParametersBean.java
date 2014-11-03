package fr.openwide.core.basicapp.core.business.audit.model.search;

import fr.openwide.core.basicapp.core.business.user.model.User;

public class AuditSearchParametersBean {
	
	private final User user;
	
	public AuditSearchParametersBean(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
