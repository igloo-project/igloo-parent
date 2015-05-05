package fr.openwide.core.jpa.security.business.person.model;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.UniqueConstraint;

import org.bindgen.Bindable;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.SortComparator;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Indexed;
import org.springframework.security.acls.model.Permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryType;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.commons.util.collections.CollectionUtils;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.search.bridge.GenericEntityCollectionIdFieldBridge;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.person.util.AbstractPersonGroupComparator;

@Indexed
@MappedSuperclass
@Bindable
@NaturalIdCache
public abstract class GenericUser<U extends GenericUser<U, G>, G extends GenericUserGroup<G, U>>
		extends GenericEntity<Long, U>
		implements IGroupedUser<G> {

	private static final long serialVersionUID = 1803671157183603979L;
	
	public static final String USER_NAME_SORT_FIELD_NAME = "userNameSort";
	
	@Id
	@GeneratedValue
	@DocumentId
	private Long id;
	
	@Column(nullable = false)
	@NaturalId(mutable = true)
	@Fields({
		@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT)),
		@Field(name = USER_NAME_SORT_FIELD_NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT))
	})
	private String userName;
	
	@JsonIgnore
	private String passwordHash = "*NO PASSWORD*";
	
	@Field
	private boolean active = true;
	
	@JsonIgnore
	@Column(nullable = false)
	private Date creationDate = new Date();
	
	@JsonIgnore
	@Column(nullable = false)
	private Date lastUpdateDate = new Date();
	
	@JsonIgnore
	private Date lastLoginDate;
	
	/**
	 * preferred locale for user, can be null
	 */
	@JsonIgnore
	private Locale locale;
	
	@JsonIgnore
	@ManyToMany
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private Set<Authority> authorities = new LinkedHashSet<Authority>();
	
	@ManyToMany
	@JoinTable(uniqueConstraints = { @UniqueConstraint(columnNames = { "persons_id", "groups_id" }) })
	@SortComparator(AbstractPersonGroupComparator.class)
	@Field(bridge = @FieldBridge(impl = GenericEntityCollectionIdFieldBridge.class), analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private Set<G> groups = Sets.newTreeSet(AbstractPersonGroupComparator.get());
	
	public GenericUser() {
	}
	
	public GenericUser(String userName, String passwordHash) {
		this.userName = userName;
		this.passwordHash = passwordHash;
	}
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public abstract String getFullName();

	@Override
	public Set<Authority> getAuthorities() {
		return Collections.unmodifiableSet(authorities);
	}

	public void setAuthorities(Set<Authority> authorities) {
		CollectionUtils.replaceAll(this.authorities, authorities);
	}
	
	public void addAuthority(Authority authority) {
		this.authorities.add(authority);
	}
	
	public void removeAuthority(Authority authority) {
		this.authorities.remove(authority);
	}
	
	@Override
	public Set<G> getGroups() {
		return Collections.unmodifiableSet(groups);
	}

	public void setGroups(Set<G> groups) {
		CollectionUtils.replaceAll(this.groups, groups);
	}

	@Override
	public void addGroup(G group) {
		this.groups.add(group);
	}
	
	@Override
	public void removeGroup(G personGroup) {
		this.groups.remove(personGroup);
	}
	
	@Override
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = CloneUtils.clone(lastLoginDate);
	}

	public Date getLastLoginDate() {
		return CloneUtils.clone(lastLoginDate);
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = CloneUtils.clone(creationDate);
	}

	public Date getCreationDate() {
		return CloneUtils.clone(creationDate);
	}
	
	public Date getLastUpdateDate() {
		return CloneUtils.clone(lastUpdateDate);
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = CloneUtils.clone(lastUpdateDate);
	}

	/**
	 * Fournit la locale préférée de l'utilisateur. Il faut utiliser
	 * {@link CoreConfigurer##toAvailableLocale(Locale)} si la locale
	 * préférée de l'utilisateur doit être exploitée pour choisir des traductions.
	 * Cette méthode permet de mapper une locale quelconque (incluant null) sur
	 * une locale qui sera obligatoirement reconnue pas le système (de manière à
	 * avoir un fonctionnement prédictible). 
	 * @return une locale, possiblement null
	 */
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	@Override
	@QueryType(PropertyType.NONE)
	public Set<? extends Permission> getPermissions() {
		return Sets.newHashSetWithExpectedSize(0);
	}

	@Override
	public int compareTo(U user) {
		if(this.equals(user)) {
			return 0;
		}
		return DEFAULT_STRING_COLLATOR.compare(this.getUserName(), user.getUserName());
	}

	@Override
	public String getNameForToString() {
		return getUserName();
	}

	@Override
	public String getDisplayName() {
		return getUserName();
	}
}