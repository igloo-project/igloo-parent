package org.iglooproject.jpa.security.business.person.model;

import java.util.Set;

import org.bindgen.Bindable;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.springframework.security.acls.model.Permission;

@Bindable
public interface IUser {

	Long getId();

	String getUsername();

	Set<Authority> getAuthorities();

	Set<? extends Permission> getPermissions();

	boolean isActive();

	String getPasswordHash();

}
