package org.iglooproject.jpa.business.generic.model;

import java.io.Serializable;

/**
 * A serializable reference to a {@link IReferenceable} (generally an entity).
 */
public interface IReference<E> extends IReferenceable<E>, Serializable {
	
	/**
	 * The type that is referenced.
	 * <p>IDs are supposed to be unique among all instances of a given type.
	 */
	Class<? extends E> getType();
	
	boolean matches(E referenceable);
	
}
