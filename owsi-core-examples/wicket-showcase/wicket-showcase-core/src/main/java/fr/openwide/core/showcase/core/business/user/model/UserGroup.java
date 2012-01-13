package fr.openwide.core.showcase.core.business.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.bindgen.Bindable;

import fr.openwide.core.jpa.security.business.person.model.AbstractPersonGroup;

@Entity
@Bindable
public class UserGroup extends AbstractPersonGroup<UserGroup, User> {
	private static final long serialVersionUID = 1218812080652289263L;
	
	@ManyToMany(mappedBy = "userGroups")
	private List<User> users = new ArrayList<User>();
	
	public UserGroup() {
		super();
	}
	
	public UserGroup(String name) {
		super(name);
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public void addUser(User user) {
		if (!users.contains(user)) {
			users.add(user);
			user.addUserGroup(this);
		}
	}
	
	public void removeUser(User user) {
		if (users.contains(user)) {
			users.remove(user);
			user.removeUserGroup(this);
		}
	}
}
