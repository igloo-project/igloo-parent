package fr.openwide.core.jpa.business.generic.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.springframework.util.Assert;

import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;

@Embeddable
@MappedSuperclass
@Access(AccessType.FIELD)
public class GenericEntityReference<K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>>
		implements Serializable {
	
	private static final long serialVersionUID = 1357434247523209721L;
	
	private static final String CLASS_TYPE = "org.hibernate.type.ClassType";

	@Column(nullable = true)
	@Type(type = CLASS_TYPE)
	private /* final */ Class<? extends E> type;
	
	@Column(nullable = true)
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private /* final */ K id;

	public static <K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>> GenericEntityReference<K, E> of(E entity) {
		return entity == null || entity.isNew() ? null : new GenericEntityReference<K, E>(entity);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <E extends GenericEntity<?, ?>> GenericEntityReference<?, E> ofUnknownIdType(E entity) {
		return entity == null || entity.isNew() ? null : (GenericEntityReference<?, E>) new GenericEntityReference(entity);
	}

	public static <K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>> GenericEntityReference<K, E> of(
			Class<? extends E> entityClass, K entityId) {
		return new GenericEntityReference<K, E>(entityClass, entityId);
	}
	
	protected GenericEntityReference() { } // Pour Hibernate
	
	@SuppressWarnings("unchecked")
	public GenericEntityReference(E entity) {
		Assert.notNull(entity, "The referenced entity must not be null");
		Assert.state(!entity.isNew(), "The referenced entity must not be transient");
		this.type = (Class<? extends E>)Hibernate.getClass(entity);
		this.id = entity.getId();
	}
	
	public GenericEntityReference(Class<? extends E> entityClass, K entityId) {
		super();
		Assert.notNull(entityClass, "entityClass must not be null");
		Assert.notNull(entityId, "entityId must not be null");
		this.type = entityClass;
		this.id = entityId;
	}

	public Class<? extends E> getType() {
		return type;
	}

	public K getId() {
		return id;
	}

	/**
	 * @deprecated Use {@link #getType()} instead.
	 */
	@Deprecated
	public Class<? extends E> getEntityClass() {
		return getType();
	}

	/**
	 * @deprecated Use {@link #getId()} instead.
	 */
	@Deprecated
	public K getEntityId() {
		return getId();
	}

	@SuppressWarnings("unchecked")
	protected static <K extends Comparable<K> & Serializable> Class<? extends GenericEntity<K, ?>> getUpperEntityClass(Class<? extends GenericEntity<K, ?>> entityClass) {
		Class<?> currentClass = entityClass;
		while (currentClass != null && currentClass.getAnnotation(Entity.class) == null) {
			currentClass = currentClass.getSuperclass();
		}
		return (Class<? extends GenericEntity<K, ?>>) currentClass;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		
		/* Caution here: we really need an instanceof, not a this.getClass() == other.getClass()
		 * because some subclasses may simply be workarounds (for instance HistoryEntityReference in JPA-More)
		 */
		if (!(obj instanceof GenericEntityReference)) {
			return false;
		}
		GenericEntityReference<?, ? extends GenericEntity<?, ?>> other = (GenericEntityReference<?, ?>) obj;
		return new EqualsBuilder()
				.append(getId(), other.getId())
				.append(getUpperEntityClass(getType()), getUpperEntityClass(other.getType()))
				.build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getId())
				.append(getUpperEntityClass(getType()))
				.build();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("class", getType())
				.append("id", getId())
				.build();
	}

}