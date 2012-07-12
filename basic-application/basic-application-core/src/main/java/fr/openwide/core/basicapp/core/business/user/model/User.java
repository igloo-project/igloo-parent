package fr.openwide.core.basicapp.core.business.user.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.model.IPersonGroup;
import fr.openwide.core.spring.util.StringUtils;

@Indexed
@Bindable
@Cacheable
@Entity(name = "user_")
public class User extends AbstractPerson<User> {
	
	private static final long serialVersionUID = 1508647513049577617L;
	
	@ManyToMany
	@JoinTable(uniqueConstraints = { @UniqueConstraint(columnNames = { "persons_id", "usergroups_id" }) })
	private List<UserGroup> userGroups = Lists.newArrayList();
	
	public User() {
		super();
	}
	
	public List<UserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IPersonGroup> getPersonGroups() {
		return (List<IPersonGroup>) (Object) getUserGroups();
	}
	
	@Override
	public String getFullName() {
		String fullName = super.getFullName();
		if (StringUtils.hasText(fullName)) {
			return fullName;
		} else {
			return getEmail();
		}
	}
}
