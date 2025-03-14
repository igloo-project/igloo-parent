package org.iglooproject.jpa.security.business.user.service;

import org.iglooproject.jpa.security.business.user.model.IUser;

public interface ICoreUserSecurityService<U extends IUser> {

  U getByUsername(String username);

  U getByUsernameCaseInsensitive(String username);
}
