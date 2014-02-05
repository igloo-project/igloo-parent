package fr.openwide.core.test.jpa.security.business.person.model;

import javax.persistence.Entity;

import fr.openwide.core.jpa.security.business.person.model.GenericUserGroup;

@Entity
public class MockUserGroup extends GenericUserGroup<MockUserGroup, MockUser> {
	private static final long serialVersionUID = 5310917945553360988L;

	@Override
	protected MockUserGroup thisAsConcreteType() {
		return this;
	}

}
