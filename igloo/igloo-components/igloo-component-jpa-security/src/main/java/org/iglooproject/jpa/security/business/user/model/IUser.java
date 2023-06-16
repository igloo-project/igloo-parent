package org.iglooproject.jpa.security.business.user.model;

import java.util.Set;

import org.bindgen.Bindable;
import org.iglooproject.jpa.security.business.authority.model.Authority;

@Bindable
public interface IUser {

	Long getId();

	String getUsername();

	Set<Authority> getAuthorities();

	boolean isEnabled();

	String getPasswordHash();

}
