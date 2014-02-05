package fr.openwide.core.jpa.security.business.person.model;

import java.util.Set;

import org.bindgen.Bindable;
import org.springframework.security.acls.model.Permission;

import fr.openwide.core.jpa.security.business.authority.model.Authority;

@Bindable
public interface IUser {
	
	Long getId();
	
	String getUserName();

	Set<Authority> getAuthorities();

	Set<? extends Permission> getPermissions();

	boolean isActive();

	String getPasswordHash();

}
