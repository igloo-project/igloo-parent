package org.iglooproject.jpa.security.business.person.service;

import org.iglooproject.jpa.security.business.person.model.GenericUser;

public interface ISecurityUserService<U extends GenericUser<U, ?>> {

	U getByUsername(String username);

	U getByUsernameCaseInsensitive(String username);

}
