package org.iglooproject.jpa.security.business.user.service;

import org.iglooproject.jpa.security.business.user.model.GenericUser;

public interface ISecurityUserService<U extends GenericUser<U, ?>> {

  U getByUsername(String username);

  U getByUsernameCaseInsensitive(String username);
}
