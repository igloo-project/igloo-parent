package org.iglooproject.showcase.core.business.user.model;

import javax.persistence.Entity;

import org.bindgen.Bindable;

import org.iglooproject.jpa.security.business.person.model.GenericUserGroup;

@Entity
@Bindable
public class UserGroup extends GenericUserGroup<UserGroup, User> {
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
