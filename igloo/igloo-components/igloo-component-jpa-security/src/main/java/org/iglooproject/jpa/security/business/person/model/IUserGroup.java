package org.iglooproject.jpa.security.business.person.model;

import java.util.Set;

import org.bindgen.Bindable;
import org.springframework.security.acls.model.Permission;

import org.iglooproject.jpa.security.business.authority.model.Authority;

@Bindable
public interface IUserGroup {
	
	Long getId();
	
	String getName();

	Set<Authority> getAuthorities();
	
	Set<? extends Permission> getPermissions();

}
