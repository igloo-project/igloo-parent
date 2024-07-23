package org.iglooproject.test.jpa.security.business.person.model;

import javax.persistence.Entity;
import org.hibernate.search.annotations.Indexed;
import org.iglooproject.jpa.security.business.user.model.GenericSimpleUser;

@Entity
@Indexed
public class MockUser extends GenericSimpleUser<MockUser, MockUserGroup> {

  private static final long serialVersionUID = 4396833928821998996L;
}
