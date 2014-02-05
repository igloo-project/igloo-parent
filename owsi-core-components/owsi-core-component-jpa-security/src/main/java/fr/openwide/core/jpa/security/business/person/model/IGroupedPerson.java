package fr.openwide.core.jpa.security.business.person.model;

import java.util.Set;

public interface IGroupedPerson<G extends IPersonGroup> extends IPerson {
	
	Set<G> getGroups();
	
	void addGroup(G personGroup);
	
	void removeGroup(G personGroup);

}
