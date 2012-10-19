package fr.openwide.core.showcase.core.business.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.model.IPersonGroup;

@Entity(name="user_")
@Bindable
@Indexed
@Cacheable
public class User extends AbstractPerson<User> {
	private static final long serialVersionUID = 7809996026983881824L;
	
	@ManyToMany
	private List<UserGroup> userGroups = new ArrayList<UserGroup>();
	
	@Column(nullable = false)
	private Integer position;
	
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
	
	public Integer getPosition() {
		return position;
	}
	
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT))
	public String getSortName() {
		StringBuilder builder = new StringBuilder();
		if(getLastName() != null) {
			builder.append(getLastName());
			builder.append(" ");
		}
		if(getFirstName() != null) {
			builder.append(getFirstName());
		}
		return builder.toString().trim();
	}
}
