package org.iglooproject.test.jpa.security.business.person.model;

import javax.persistence.Entity;
import org.iglooproject.jpa.security.business.user.model.GenericUserGroup;

@Entity
public class MockUserGroup extends GenericUserGroup<MockUserGroup, MockUser> {

  private static final long serialVersionUID = 5310917945553360988L;

  @Override
  protected MockUserGroup thisAsConcreteType() {
    return this;
  }
}
