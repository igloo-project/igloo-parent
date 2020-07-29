package org.iglooproject.jpa.security.business.authority.model;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bindgen.Bindable;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Sets;

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
	public int compareTo(Authority authority) {
		if(this == authority) {
			return 0;
		}
		return this.getName().compareToIgnoreCase(authority.getName());
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()
			.add("name", getName());
	}

}