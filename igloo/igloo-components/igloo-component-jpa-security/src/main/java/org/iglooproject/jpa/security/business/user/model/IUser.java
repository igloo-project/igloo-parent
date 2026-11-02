package org.iglooproject.jpa.security.business.user.model;

import java.util.Locale;
import org.bindgen.Bindable;

@Bindable
public interface IUser extends ISessionUser {

  Long getId();

  @Override
  String getUsername();

  String getPasswordHash();

  Locale getLocale();

  boolean isEnabled();
}
