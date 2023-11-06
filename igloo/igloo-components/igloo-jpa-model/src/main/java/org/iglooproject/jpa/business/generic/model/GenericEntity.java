/*
 * Copyright (C) 2009-2010 Open Wide
 * Contact: contact@openwide.fr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.iglooproject.jpa.business.generic.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.function.Function;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.Hibernate;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.iglooproject.commons.util.ordering.SerializableCollator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Ordering;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

@MappedSuperclass
public abstract class GenericEntity<K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>>
		implements Serializable, Comparable<E>, IReferenceable<E>, IGenericEntityBindingInterface {

	private static final long serialVersionUID = -3988499137919577054L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericEntity.class);

	/**
	 * This function allows to implement different behaviors based on Hibernate availability on classpath.
	 * {@link Hibernate}.getClass(Object) is needed when we want to compare entity proxies.
	 */
	static final Function<Object, Class<?>> GET_CLASS_FUNCTION;
	public static final GenericEntityImplementation IMPLEMENTATION;

	public static final String ID = "id";

	@SuppressWarnings("rawtypes")
	private static final Ordering<Comparable> DEFAULT_KEY_ORDERING = Ordering.natural().nullsLast();

	public static final Ordering<String> STRING_COLLATOR_FRENCH = new SerializableCollator(Locale.FRENCH).nullsLast();
	public static final Ordering<String> STRING_COLLATOR_ENGLISH = new SerializableCollator(Locale.FRENCH).nullsLast();
	public static final Ordering<String> STRING_COLLATOR_ROOT = new SerializableCollator(Locale.FRENCH).nullsLast();

	@Override
	@Transient
	@SuppressWarnings("unchecked")
	public GenericEntityReference<K, E> asReference() {
		return GenericEntityReference.of((E) this);
	}

	@Override
	@QueryType(PropertyType.COMPARABLE)
	@GenericField(name = ID, sortable = Sortable.YES, projectable = Projectable.YES)
	public abstract K getId();

	public abstract void setId(K id);

	@Override
	@JsonIgnore
	@Transient
	public boolean isNew() {
		return getId() == null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		// process class comparison; uses Hibernate for proxy handling if available
		if (GET_CLASS_FUNCTION.apply(obj) != GET_CLASS_FUNCTION.apply(this)) {
			return false;
		}
		
		@SuppressWarnings("unchecked")
		GenericEntity<K, E> entity = (GenericEntity<K, E>) obj; // NOSONAR
		
		if (entity.getId() == null) {
			return false;
		}
		
		return new EqualsBuilder()
			.append(getId(), entity.getId())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}

	@Override
	public int compareTo(E entity) {
		if (this == entity) {
			return 0;
		}
		
		K leftId = getId();
		K rightId = entity.getId();
		
		if (leftId == null && rightId == null) {
			throw new IllegalArgumentException("Cannot compare two different entities with null IDs");
		}
		
		return new CompareToBuilder()
			.append(leftId, rightId, DEFAULT_KEY_ORDERING)
			.toComparison();
	}

	/**
	 * DO NOT OVERRIDE THIS METHOD UNLESS YOU HAVE A VERY GOOD REASON FOR IT.
	 * Override {@link #toStringHelper()} instead.
	 */
	@Override
	public String toString() {
		return toStringHelper().toString();
	}

	@JsonIgnore
	protected ToStringHelper toStringHelper() {
		return MoreObjects.toStringHelper(GET_CLASS_FUNCTION.apply(this).getSimpleName())
			.add("id", getId());
	}

	/**
	 * Add a simple way to track unwanted entity serializations.
	 * <p>Note that, in some cases we <strong>do</strong> want to serialize persisted entities, for instance
	 * when they are values of attributes of an entity which is in the process of being created in a web form.
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
		if (LOGGER.isDebugEnabled() && !isNew()) {
			LOGGER.debug(
					"Serializing a persisted entity (class = {} and id = {})",
					getClass(), getId()
			);
		}
		out.defaultWriteObject();
	}

	static {
		boolean hibernateAvailable = false;
		
		try {
			Class.forName("org.hibernate.Hibernate");
			hibernateAvailable = true;
		} catch (ClassNotFoundException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("GenericEntity.equals will not be hibernate aware");
			}
			if (LOGGER.isTraceEnabled()) {
				LOGGER.debug("org.hibernate.Hibernate loading failed", e);
			}
		}
		
		if (hibernateAvailable) {
			GET_CLASS_FUNCTION = Hibernate::getClass;
			IMPLEMENTATION = GenericEntityImplementation.HIBERNATE;
		} else {
			GET_CLASS_FUNCTION = Object::getClass;
			IMPLEMENTATION = GenericEntityImplementation.SIMPLE;
		}
	}

}
