package fr.openwide.core.showcase.core.business.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.bindgen.Bindable;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.Indexed;

import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.model.IPersonGroup;

@Entity(name="user_")
@Bindable
@Indexed
public class User extends AbstractPerson<User> {
	private static final long serialVersionUID = 7809996026983881824L;
	
	@ManyToMany
	private List<UserGroup> userGroups = new ArrayList<UserGroup>();
	
	public List<UserGroup> getUserGroups() {
		return userGroups;
	}
	
	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups.clear();
		
		for (UserGroup userGroup : userGroups) {
			this.userGroups.add(userGroup);
		}
	}
	
	public void addUserGroup(UserGroup userGroup) {
		if (!userGroups.contains(userGroup)) {
			userGroups.add(userGroup);
		}
	}
	
	public void removeUserGroup(UserGroup userGroup) {
		if (userGroups.contains(userGroup)) {
			userGroups.remove(userGroup);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IPersonGroup> getPersonGroups() {
		return (List<IPersonGroup>) ((Object) userGroups);
	}

	@Override
	@Transient
	@JsonIgnore
	public String getPasswordHash() {
		return getMd5Password();
	}
}
