package org.iglooproject.wicket.more.model;

import java.io.Serializable;
import java.util.Set;

import org.iglooproject.commons.util.functional.Suppliers2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

/**
 * @deprecated use {@link CollectionCopyModel} with {@link Suppliers2} and {@link GenericEntityModel} instead : 
 * <pre>
 * {@code 
 * GenericEntityHashSetModel.of(Class<E>);
 * ->
 * CollectionCopyModel.custom(Suppliers2.<E>hashSet[AsSet](), GenericEntityModel.<E>factory());
 * }
 * </pre>
 */
@Deprecated
public class GenericEntityHashSetModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends AbstractGenericEntityCollectionModel<K, E, Set<E>> {
	
	private static final long serialVersionUID = 7391021833949792344L;

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			GenericEntityHashSetModel<K, E> of(Class<E> clazz) {
		return new GenericEntityHashSetModel<K, E>(clazz);
	}

	protected GenericEntityHashSetModel(Class<E> clazz) {
		super(clazz, Suppliers2.<E>hashSet());
	}

}
