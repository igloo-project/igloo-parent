package fr.openwide.core.basicapp.web.application.administration.model;

import fr.openwide.core.basicapp.core.business.user.model.User;

public abstract class GenericUserDataProvider<U extends User> extends AbstractUserDataProvider<U> {
	
	private static final long serialVersionUID = -8540890431031886412L;
	
	public GenericUserDataProvider(Class<U> clazz) {
		super(clazz);
	}
}
