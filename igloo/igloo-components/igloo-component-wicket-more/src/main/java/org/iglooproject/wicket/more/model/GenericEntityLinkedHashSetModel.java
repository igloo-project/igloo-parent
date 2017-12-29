package org.iglooproject.wicket.more.model;

import java.io.Serializable;
import java.util.LinkedHashSet;

import org.iglooproject.commons.util.functional.Suppliers2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

/**
 * @deprecated use {@link CollectionCopyModel} with {@link Suppliers2} and {@link GenericEntityModel} instead : 
 * <pre>
 * {@code 
 * GenericEntityLinkedHashSetModel.of(Class<E>);
 * ->
 * CollectionCopyModel.custom(Suppliers2.<E>linkedHashSet[AsSet](), GenericEntityModel.<E>factory());
 * }
 * </pre>
 */
@Deprecated
public class GenericEntityLinkedHashSetModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends AbstractGenericEntityCollectionModel<K, E, LinkedHashSet<E>> {
	
	private static final long serialVersionUID = 7391021833949792344L;

	public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
			GenericEntityLinkedHashSetModel<K, E> of(Class<E> clazz) {
		return new GenericEntityLinkedHashSetModel<K, E>(clazz);
	}

	protected GenericEntityLinkedHashSetModel(Class<E> clazz) {
		super(clazz, Suppliers2.<E>linkedHashSet());
	}

}
