package fr.openwide.core.hibernate.security.business.person.model;

import java.util.Set;

import org.bindgen.Bindable;

import fr.openwide.core.hibernate.security.business.authority.model.Authority;

@Bindable
public interface PersonGroup {
	
	Integer getId();
	
	String getName();

	Set<Authority> getAuthorities();

}
