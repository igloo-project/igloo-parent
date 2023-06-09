package org.iglooproject.jpa.security.business.user.model;

import static org.iglooproject.jpa.security.service.CoreJpaUserDetailsServiceImpl.EMPTY_PASSWORD_HASH;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

import org.bindgen.Bindable;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.SortComparator;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.user.util.GenericUserGroupComparator;
import org.springframework.security.acls.model.Permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Sets;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MappedSuperclass;

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

	public static final String ENABLED = "enabled";

	public static final String GROUPS = "groups";

	@Id
	@GeneratedValue
	// TODO igloo-boot; with hibernate-search 6.x, there is no longer a default indexed identifier
	@GenericField(name = "id", sortable = Sortable.YES)
	private Long id;

	@Column(nullable = false)
	@NaturalId(mutable = true)
	@FullTextField(name = USERNAME, analyzer = HibernateSearchAnalyzer.TEXT)
	@KeywordField(name = USERNAME_SORT, normalizer = HibernateSearchNormalizer.TEXT, sortable = Sortable.YES)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private String username;

	@JsonIgnore
	private String passwordHash = EMPTY_PASSWORD_HASH;

	@Column(nullable = false)
	@GenericField(name = ENABLED)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private boolean enabled = true;

	@JsonIgnore
	@Column(nullable = false)
	private Instant creationDate;

	@JsonIgnore
	@Column(nullable = false)
	private Instant lastUpdateDate;

	@JsonIgnore
	@Column
	private Instant lastLoginDate;

	/**
	 * preferred locale for user, can be null
	 */
	@JsonIgnore
	@Column
	private Locale locale;

	@JsonIgnore
	@ManyToMany
	@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	private Set<Authority> authorities = new LinkedHashSet<>();

	@ManyToMany
	@SortComparator(GenericUserGroupComparator.class)
	@KeywordField(name = GROUPS, valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class))
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private SortedSet<G> groups = Sets.newTreeSet(GenericUserGroupComparator.get());

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

	public void setAuthorities(Collection<Authority> authorities) {
		CollectionUtils.replaceAll(this.authorities, authorities);
	}

	public void addAuthority(Authority authority) {
		this.authorities.add(authority);
	}

	public void removeAuthority(Authority authority) {
		this.authorities.remove(authority);
	}

	@Override
	public SortedSet<G> getGroups() {
		return Collections.unmodifiableSortedSet(groups);
	}

	public void setGroups(Collection<G> groups) {
		CollectionUtils.replaceAll(this.groups, groups);
	}

	@Override
	public void addGroup(G group) {
		this.groups.add(group);
	}

	@Override
	public void removeGroup(G group) {
		this.groups.remove(group);
	}

	@Override
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public boolean hasPasswordHash() {
		return !Objects.equals(getPasswordHash(), EMPTY_PASSWORD_HASH);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Instant getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Instant creationDate) {
		this.creationDate = creationDate;
	}

	public Instant getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Instant lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Instant getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Instant lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
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
	public Set<Permission> getPermissions() {
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
