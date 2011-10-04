package fr.openwide.core.jpa.security.business.person.model;

import java.util.Set;

import org.bindgen.Bindable;

import fr.openwide.core.jpa.security.business.authority.model.Authority;

@Bindable
public interface IPersonGroup {
	
	Integer getId();
	
	String getName();

	Set<Authority> getAuthorities();

}
