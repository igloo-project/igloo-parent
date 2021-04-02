package org.iglooproject.jpa.security.business.person.model;

import java.util.Set;

public interface IGroupedUser<G extends IUserGroup> extends IUser {

	Set<G> getGroups();

	void addGroup(G group);

	void removeGroup(G group);

}
