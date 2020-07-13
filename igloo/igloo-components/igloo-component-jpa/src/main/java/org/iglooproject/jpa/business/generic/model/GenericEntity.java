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

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.SortableField;
import org.iglooproject.commons.util.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Ordering;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;

/**
 * <p>Entité racine pour la persistence des objets via JPA.</p>
 *
 * @author Open Wide
 *
 * @param <E> type de l'entité
 */
@MappedSuperclass
public abstract class GenericEntity<K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>>
		implements Serializable, Comparable<E>, IReferenceable<E>, IGenericEntityBindingInterface {

	private static final long serialVersionUID = -3988499137919577054L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericEntity.class);

	/**
	 * This function allows to implement different behaviors based on Hibernate availability on classpath.
	 * {@link Hibernate}.getClass(Object) is needed when we want to compare entity proxies.
	 */
	private static final Function<Object, Class<?>> GET_CLASS_FUNCTION;
	public static final GenericEntityImplementation IMPLEMENTATION;

	public static final String ID_SORT = "idSort";

	@SuppressWarnings("rawtypes")
	private static final Ordering<Comparable> DEFAULT_KEY_ORDERING = Ordering.natural().nullsLast();

	public static final Ordering<String> STRING_COLLATOR_FRENCH = LocaleUtils.initCollator(Locale.FRENCH);
	public static final Ordering<String> STRING_COLLATOR_ENGLISH = LocaleUtils.initCollator(Locale.ENGLISH);
	public static final Ordering<String> STRING_COLLATOR_ROOT = LocaleUtils.initCollator(Locale.ROOT);

	@Override
	@Transient
	@SuppressWarnings("unchecked")
	public GenericEntityReference<K, E> asReference() {
		return GenericEntityReference.of((E)this);
	}

	/**
	 * Retourne la valeur de l'identifiant unique.
	 * 
	 * @return id
	 */
	@Override
	@QueryType(PropertyType.COMPARABLE)
	@Field(name = ID_SORT, analyze = Analyze.NO)
	@SortableField(forField = ID_SORT)
	public abstract K getId();

	/**
	 * Définit la valeur de l'identifiant unique.
	 * 
	 * @param id id
	 */
	public abstract void setId(K id);
	
	/**
	 * Indique si l'objet a déjà été persisté ou non
	 * 
	 * @return vrai si l'objet n'a pas encore été persisté
	 */
	@Override
	@JsonIgnore
	@Transient
	public boolean isNew() {
		return getId() == null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		
		// process class comparison; uses Hibernate for proxy handling if available
		if (GET_CLASS_FUNCTION.apply(object) != GET_CLASS_FUNCTION.apply(this)) {
			return false;
		}

		GenericEntity<K, E> entity = (GenericEntity<K, E>) object; // NOSONAR : traité au-dessus mais wrapper Hibernate 
		K id = getId();

		if (id == null) {
			return false;
		}

		return id.equals(entity.getId());
	}

	@Override
	public int hashCode() {
		int hash = 7;
		
		K id = getId();
		hash = 31 * hash + ((id == null) ? 0 : id.hashCode());

		return hash;
	}

	@Override
	public int compareTo(E right) {
		if (this == right) {
			return 0;
		}
		K leftId = getId();
		K rightId = right.getId();
		if (leftId == null && rightId == null) {
			throw new IllegalArgumentException("Cannot compare two different entities with null IDs");
		}
		return DEFAULT_KEY_ORDERING.compare(leftId, rightId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("entity.");
		builder.append(GET_CLASS_FUNCTION.apply(this).getSimpleName());
		builder.append("<");
		builder.append(getId());
		builder.append("-");
		builder.append(getNameForToString());
		builder.append(">");
		
		return builder.toString();
	}
	
	/**
	 * Retourne l'élément de chaîne qui va être injecté dans le toString() de l'objet : faire en sorte que cela permette
	 * de l'identifier.
	 *  
	 * @return chaîne à injecter dans le toString()
	 */
	@JsonIgnore
	public abstract String getNameForToString();

	/**
	 * Retourne le nom à afficher.
	 * 
	 * @return nom à afficher
	 */
	@JsonIgnore
	public abstract String getDisplayName();
	
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
			GET_CLASS_FUNCTION = i -> i.getClass();
			IMPLEMENTATION = GenericEntityImplementation.SIMPLE;
		}
	}

}
