package org.iglooproject.basicapp.core.business.user.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;
import org.iglooproject.jpa.security.business.user.model.GenericUserGroup;

@Entity
@Indexed
@Bindable
@Cacheable
public class UserGroup extends GenericUserGroup<UserGroup, User> {

  private static final long serialVersionUID = 2156717229285615454L;

  public UserGroup() {}

  public UserGroup(String name) {
    super(name);
  }

  @Override
  protected UserGroup thisAsConcreteType() {
    return this;
  }
}
