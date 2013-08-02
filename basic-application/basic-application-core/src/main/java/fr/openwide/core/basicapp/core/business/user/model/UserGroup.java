package fr.openwide.core.basicapp.core.business.user.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;

import fr.openwide.core.jpa.security.business.person.model.AbstractPersonGroup;

@Entity
@Indexed
@Bindable
@Cacheable
public class UserGroup extends AbstractPersonGroup<UserGroup, User> {
	private static final long serialVersionUID = 2156717229285615454L;

	public UserGroup() {
	}

	public UserGroup(String name) {
		super(name);
	}

}