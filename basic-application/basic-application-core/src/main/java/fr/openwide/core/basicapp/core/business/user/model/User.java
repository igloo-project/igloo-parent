package fr.openwide.core.basicapp.core.business.user.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

import org.bindgen.Bindable;
import org.hibernate.annotations.SortComparator;
import org.hibernate.search.annotations.Indexed;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import fr.openwide.core.commons.util.collections.CollectionUtils;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.model.IPersonGroup;
import fr.openwide.core.jpa.security.business.person.util.AbstractPersonGroupComparator;
import fr.openwide.core.spring.util.StringUtils;

@Indexed
@Bindable
@Cacheable
@Entity(name = "user_")
public class User extends AbstractPerson<User> {
	
	private static final long serialVersionUID = 1508647513049577617L;
	
	public static final int MIN_PASSWORD_LENGTH = 6;
	public static final int MAX_PASSWORD_LENGTH = 15;
	
	@ManyToMany
	@JoinTable(uniqueConstraints = { @UniqueConstraint(columnNames = { "persons_id", "usergroups_id" }) })
	@SortComparator(AbstractPersonGroupComparator.class)
	private Set<UserGroup> userGroups = Sets.newTreeSet(AbstractPersonGroupComparator.get());
	
	public User() {
		super();
	}
	
	public Set<UserGroup> getUserGroups() {
		return ImmutableSet.copyOf(userGroups);
	}

	public void setUserGroups(Set<UserGroup> userGroups) {
		CollectionUtils.replaceAll(this.userGroups, userGroups);
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
	
	@Override
	public void addPersonGroup(IPersonGroup personGroup) {
		if (personGroup instanceof UserGroup) {
			this.userGroups.add((UserGroup) personGroup);
		}
	}
	
	@Override
	public void removePersonGroup(IPersonGroup personGroup) {
		this.userGroups.remove(personGroup);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IPersonGroup> getPersonGroups() {
		return ImmutableList.copyOf((Set<IPersonGroup>) (Object) userGroups);
	}
	
}
