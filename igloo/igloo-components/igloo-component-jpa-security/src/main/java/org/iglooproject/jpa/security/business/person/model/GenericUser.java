package org.iglooproject.jpa.security.business.person.model;

import static org.iglooproject.jpa.security.service.CoreJpaUserDetailsServiceImpl.EMPTY_PASSWORD_HASH;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
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
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Normalizer;
import org.hibernate.search.annotations.SortableField;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.search.bridge.GenericEntityCollectionIdFieldBridge;
import org.iglooproject.jpa.search.util.HibernateSearchAnalyzer;
import org.iglooproject.jpa.search.util.HibernateSearchNormalizer;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.person.util.AbstractPersonGroupComparator;
import org.springframework.security.acls.model.Permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Sets;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;

import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;

@Indexed
@MappedSuperclass
@Bindable
@NaturalIdCache
public abstract class GenericUser<U extends GenericUser<U, G>, G extends GenericUserGroup<G, U>>
		extends GenericEntity<Long, U>
		implements IGroupedUser<G> {

	private static final long serialVersionUID = 1803671157183603979L;
	
	public static final String USERNAME = "username";
	public static final String USERNAME_SORT = "usernameSort";
	
	public static final String ACTIVE = "active";
	
	public static final String GROUPS = "groups";
	
	@Id
	@GeneratedValue
	@DocumentId
	private Long id;
	
	@Column(nullable = false)
	@NaturalId(mutable = true)
	@Field(name = USERNAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT))
	@Field(name = USERNAME_SORT, normalizer = @Normalizer(definition = HibernateSearchNormalizer.TEXT))
	@SortableField(forField = USERNAME_SORT)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private String username;
	
	@JsonIgnore
	private String passwordHash = EMPTY_PASSWORD_HASH;
	
	@Field(name = ACTIVE)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
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
	private Set<Authority> authorities = new LinkedHashSet<>();
	
	@ManyToMany
	@JoinTable(uniqueConstraints = { @UniqueConstraint(columnNames = { "persons_id", "groups_id" }) })
	@SortComparator(AbstractPersonGroupComparator.class)
	@Field(name = GROUPS, bridge = @FieldBridge(impl = GenericEntityCollectionIdFieldBridge.class), analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private Set<G> groups = Sets.newTreeSet(AbstractPersonGroupComparator.get());
	
	public GenericUser() {
	}
	
	public GenericUser(String username, String passwordHash) {
		this.username = username;
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
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	@ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
	public boolean hasPasswordHash() {
		return !Objects.equals(getPasswordHash(), EMPTY_PASSWORD_HASH);
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
	public Date getLastLoginDate() {
		return CloneUtils.clone(lastLoginDate);
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = CloneUtils.clone(lastLoginDate);
	}

	@ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
	public Date getCreationDate() {
		return CloneUtils.clone(creationDate);
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = CloneUtils.clone(creationDate);
	}

	@ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
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
	@ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
	public Set<? extends Permission> getPermissions() {
		return Sets.newHashSetWithExpectedSize(0);
	}

	@Override
	public int compareTo(U user) {
		if (this.equals(user)) {
			return 0;
		}
		return STRING_COLLATOR_FRENCH.compare(this.getUsername(), user.getUsername());
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()
			.add("username", getUsername());
	}

}
