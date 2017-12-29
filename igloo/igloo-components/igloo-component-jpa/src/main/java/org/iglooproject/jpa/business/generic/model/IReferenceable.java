package org.iglooproject.jpa.business.generic.model;


/**
 * An object that can provide a {@link IReference} that will refer to the same object as itself.
 * <p>This interface is implemented by both {@link GenericEntity} and {@link GenericEntityReference}, 
 */
public interface IReferenceable<E> {
	
	Object getId();

	IReference<E> asReference();

}
