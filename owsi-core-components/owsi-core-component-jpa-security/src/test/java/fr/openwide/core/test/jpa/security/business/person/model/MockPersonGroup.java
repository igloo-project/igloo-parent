package fr.openwide.core.test.jpa.security.business.person.model;

import javax.persistence.Entity;

import fr.openwide.core.jpa.security.business.person.model.AbstractPersonGroup;

@Entity
public class MockPersonGroup extends AbstractPersonGroup<MockPersonGroup, MockPerson> {
	private static final long serialVersionUID = 5310917945553360988L;

}
