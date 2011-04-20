package fr.openwide.core.jpa.security.business.person.model;

import java.util.List;
import java.util.Set;

import org.bindgen.Bindable;

import fr.openwide.core.jpa.security.business.authority.model.Authority;

@Bindable
public interface Person {
	
	Integer getId();
	
	String getUserName();
	
	String getFirstName();
	
	String getLastName();
	
	String getFullName();
	
	List<PersonGroup> getPersonGroups();

	Set<Authority> getAuthorities();

	boolean isActive();

	String getMd5Password();

}
