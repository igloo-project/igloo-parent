package org.iglooproject.jpa.security.business.user.model;

import java.util.Set;
import org.bindgen.Bindable;
import org.iglooproject.jpa.security.business.authority.model.Authority;

@Bindable
public interface IUserGroup {

  Long getId();

  String getName();

  Set<Authority> getAuthorities();
}
