package org.iglooproject.test.jpa.security.business.person.model;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.security.business.user.model.GenericSimpleUser;

import jakarta.persistence.Entity;

@Entity
@Indexed
public class MockUser extends GenericSimpleUser<MockUser, MockUserGroup> {

	private static final long serialVersionUID = 4396833928821998996L;

}
