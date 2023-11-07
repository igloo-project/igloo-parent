package org.iglooproject.jpa.security.business.authority.model;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bindgen.Bindable;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Sets;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Bindable
public class Authority extends GenericEntity<Long, Authority> {

	private static final long serialVersionUID = -7704280784189665811L;

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String name;

	@ElementCollection
	private Set<String> customPermissionNames = Sets.newHashSet();

	public Authority() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Authority(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getCustomPermissionNames() {
		return Collections.unmodifiableSet(customPermissionNames);
	}

	public void setCustomPermissionNames(Set<String> customPermissionNames) {
		CollectionUtils.replaceAll(this.customPermissionNames, customPermissionNames);
	}

	public boolean addCustomPermissionName(String customPermissionName) {
		return customPermissionNames.add(customPermissionName);
	}

	public boolean removeCustomPermissionName(String customPermissionName) {
		return customPermissionNames.remove(customPermissionName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Authority)) {
			return false;
		}
		Authority other = (Authority) obj;
		return new EqualsBuilder()
			.append(getName(), other.getName())
			.appendSuper(super.equals(other))
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getName())
			.appendSuper(super.hashCode())
			.toHashCode();
	}

	@Override
	public int compareTo(Authority authority) {
		return new CompareToBuilder()
			.append(getName(), authority.getName())
			.appendSuper(super.compareTo(authority))
			.toComparison();
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()
			.add("name", getName());
	}

}
