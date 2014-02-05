package fr.openwide.core.showcase.core.business.user.model;

import javax.persistence.Entity;

import org.bindgen.Bindable;

import fr.openwide.core.jpa.security.business.person.model.AbstractPersonGroup;

@Entity
@Bindable
public class UserGroup extends AbstractPersonGroup<UserGroup, User> {
	private static final long serialVersionUID = 1218812080652289263L;
	
	public UserGroup() {
		super();
	}
	
	public UserGroup(String name) {
		super(name);
	}

	@Override
	protected UserGroup thisAsConcreteType() {
		return this;
	}
}
