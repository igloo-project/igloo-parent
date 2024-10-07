package org.iglooproject.test.jpa.security.business.person.model;

import jakarta.persistence.Entity;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.security.business.user.model.GenericSimpleUser;

@Entity
@Indexed
public class MockUser extends GenericSimpleUser<MockUser> {

  private static final long serialVersionUID = 4396833928821998996L;
}
