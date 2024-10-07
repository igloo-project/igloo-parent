package org.iglooproject.jpa.security.business.user.model;

import org.bindgen.Bindable;

@Bindable
public interface IUser {

  Long getId();

  String getUsername();

  boolean isEnabled();

  String getPasswordHash();
}
