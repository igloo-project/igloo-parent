package org.iglooproject.jpa.security.business.user.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;

import org.bindgen.Bindable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.SortComparator;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Normalizer;
import org.hibernate.search.annotations.SortableField;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.user.util.GenericUserComparator;
import org.springframework.security.acls.model.Permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;

@MappedSuperclass
@Bindable
public abstract class GenericUserGroup<G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
		extends GenericEntity<Long, G>
		implements IUserGroup {

	private static final long serialVersionUID = 2156717229285615454L;

	public static final String NAME = "name";
	public static final String NAME_SORT = "nameSort";

	@Id
	@DocumentId
	@GeneratedValue
	private Long id;

	@Column
	@Field(name = NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT))
	@Field(name = NAME_SORT, normalizer = @Normalizer(definition = HibernateSearchNormalizer.TEXT))
	@SortableField(forField = NAME_SORT)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private String name;

	/**
	 * This field is here just to generate bindings (qGenericUserGroup).
	 * <p>It should not be accessed, since:
	 * <ol>
	 * <li>it is not kept up to date when adding a user to a group</li>
	 * <li>loading it or keeping it up to date may lead to performance issues when groups contain many users (> 10k).
	 * </ol>
	 */
	@JsonIgnore
	@ManyToMany(mappedBy = "groups")
	@SortComparator(GenericUserComparator.class)
	private SortedSet<U> users = Sets.newTreeSet(GenericUserComparator.get()); // NOSONAR

	@JsonIgnore
	@ManyToMany
	@Cascade({ CascadeType.SAVE_UPDATE })
	@OrderBy("name")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Authority> authorities = new LinkedHashSet<>();

	@Column
	@Type(type = "org.iglooproject.jpa.hibernate.usertype.StringClobType")	
	private String description;

	@Column(nullable = false)
	private boolean locked = false;

	public GenericUserGroup() {
	}

	public GenericUserGroup(String name) {
		this.name = name;
	}
	
	protected abstract G thisAsConcreteType();

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	@QueryType(PropertyType.NONE)
	public Set<Permission> getPermissions() {
		return Sets.newHashSetWithExpectedSize(0);
	}

	@Override
	public int compareTo(G group) {
		if (this == group) {
			return 0;
		}
		return STRING_COLLATOR_FRENCH.compare(this.getName(), group.getName());
	}

}
